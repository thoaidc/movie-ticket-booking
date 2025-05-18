package vn.ptit.moviebooking.ticket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import vn.ptit.moviebooking.ticket.constants.HttpStatusConstants;
import vn.ptit.moviebooking.ticket.constants.RabbitMQConstants;
import vn.ptit.moviebooking.ticket.constants.WebSocketConstants;
import vn.ptit.moviebooking.ticket.dto.request.BookingRequest;
import vn.ptit.moviebooking.ticket.dto.request.CheckSeatAvailabilityRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ConfirmSeatsRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.PaymentRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.RefundRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ReserveSeatsRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ValidateMovieRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.VerifyCustomerRequestCommand;
import vn.ptit.moviebooking.ticket.dto.response.BaseCommandReplyMessage;
import vn.ptit.moviebooking.ticket.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.ticket.service.rabbitmq.RabbitMQProducer;

@Service
@DependsOn("bindingQueues")
public class SagaCommandProducer {

    private static final Logger log = LoggerFactory.getLogger(SagaCommandProducer.class);
    private final RabbitMQProducer rabbitMQProducer;
    private final TicketBookingService ticketBookingService;
    private final WebSocketNotificationService notificationService;
    private final ObjectMapper objectMapper;

    public SagaCommandProducer(RabbitMQProducer rabbitMQProducer,
                               TicketBookingService ticketBookingService,
                               WebSocketNotificationService notificationService,
                               ObjectMapper objectMapper) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.ticketBookingService = ticketBookingService;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    public BaseResponseDTO createBookingSagaOrchestrator(BookingRequest bookingRequest) {
        // Create booking with PENDING status
        ValidateMovieRequestCommand command = ticketBookingService.createValidateMovieCommand(bookingRequest);
        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.MOVIE_VALIDATE_COMMAND, command);
        return BaseResponseDTO.builder().ok("Create booking request successfully!");
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.MOVIE_VALIDATE_REPLY)
    public void handleMovieValidateReply(String message, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Invalid movie information! Cancel booking...")
                .build();

        try {
            BaseCommandReplyMessage replyMessage = objectMapper.readValue(message, BaseCommandReplyMessage.class);

            // Movie validate successfully -> Check availability seats + Hold seats to payment
            if (replyMessage.isSuccess()) {
                CheckSeatAvailabilityRequestCommand command =
                        ticketBookingService.createCheckSeatsAvailabilityCommand(replyMessage);

                response = BaseResponseDTO.builder().message("Movie verified successfully!").ok();
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.CHECK_SEAT_AVAILABILITY_COMMAND, command);
            } else {
                // Movie validate failed -> Cancel booking
                ticketBookingService.cancelBooking(replyMessage);
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from movie-service when validate movie", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.CHECK_SEAT_AVAILABILITY_REPLY)
    public void handleSeatsAvailabilityCheckingReply(String message, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Seat held failed! Cancel booking...")
                .build();

        try {
            BaseCommandReplyMessage replyMessage = objectMapper.readValue(message, BaseCommandReplyMessage.class);

            // Seats availability + Held seats successfully -> Verify customer + Save customer info
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Seat held successfully!").ok();
                VerifyCustomerRequestCommand command = ticketBookingService.createVerifyCustomerCommand(replyMessage);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.VERIFY_CUSTOMER_COMMAND, command);
            } else {
                // Seats unavailability / Held seats failed -> Cancel booking
                ticketBookingService.cancelBooking(replyMessage);
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from seat-availability-service when checking seats availability", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.VERIFY_CUSTOMER_REPLY)
    public void handleVerifyCustomerReply(String message, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Your information verified failed! Cancel booking...")
                .build();

        try {
            BaseCommandReplyMessage replyMessage = objectMapper.readValue(message, BaseCommandReplyMessage.class);

            // Customer verified successfully -> Payment process
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Your information verified successfully!").ok();
                PaymentRequestCommand command = ticketBookingService.createPaymentProcessCommand(replyMessage);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.PAYMENT_PROCESS_COMMAND, command);
            } else {
                // Verified failed -> Reserve held booking seats + Cancel booking
                ticketBookingService.cancelBooking(replyMessage);
                ReserveSeatsRequestCommand command = ticketBookingService.createReserveBookingSeatsCommand(replyMessage);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.RESERVE_SEAT_COMMAND, command);
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from customer-service from verify customer information", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.PAYMENT_PROCESS_REPLY)
    public void handlePaymentProcessReply(String message, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Payment failed! Cancel booking...")
                .build();

        try {
            BaseCommandReplyMessage replyMessage = objectMapper.readValue(message, BaseCommandReplyMessage.class);

            // Payment successfully -> Confirm booking seats
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Payment successfully!").ok();
                ConfirmSeatsRequestCommand command = ticketBookingService.createConfirmBookingSeatsCommand(replyMessage);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.CONFIRM_SEAT_COMMAND, command);
            } else {
                // Payment failed -> Reserve held booking seats + Cancel booking
                ticketBookingService.cancelBooking(replyMessage);
                ReserveSeatsRequestCommand command = ticketBookingService.createReserveBookingSeatsCommand(replyMessage);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.RESERVE_SEAT_COMMAND, command);
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from payment-service when payment booking", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.CONFIRM_SEAT_REPLY)
    public void handleConfirmBookingSeatsReply(String message, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Confirmation of booked seat failed! Cancel booking...")
                .build();

        try {
            BaseCommandReplyMessage replyMessage = objectMapper.readValue(message, BaseCommandReplyMessage.class);

            // Booked seats successfully -> Confirm order completed
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Booking successfully!").ok();
                ticketBookingService.approveBooking(replyMessage);
            } else {
                // Booked seats failed -> Refund to customer + Reserve held booking seats + Cancel booking
                ticketBookingService.cancelBooking(replyMessage);
                ReserveSeatsRequestCommand command = ticketBookingService.createReserveBookingSeatsCommand(replyMessage);
                RefundRequestCommand refundCommand = ticketBookingService.createRefundCommand(replyMessage);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.RESERVE_SEAT_COMMAND, command);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.PAYMENT_REFUND_COMMAND, refundCommand);
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from seat-availability-service when confirm booked seats", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.RESERVE_SEAT_REPLY)
    public void handleReserveSeatsReply(String message, Channel channel, Message amqpMessage) {
        // When customer verified fail / Payment failed / Confirm booking seats failed
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Could not reserve booked seats!")
                .build();

        try {
            BaseCommandReplyMessage replyMessage = objectMapper.readValue(message, BaseCommandReplyMessage.class);

            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Reserve booked seats successfully!").ok();
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from seat-availability-service when reserve booked seats", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.PAYMENT_REFUND_REPLY)
    public void handleRefundReply(String message, Channel channel, Message amqpMessage) {
        // When confirm booking seats failed
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Refund to customer failed!")
                .build();

        try {
            BaseCommandReplyMessage replyMessage = objectMapper.readValue(message, BaseCommandReplyMessage.class);

            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Refund to customer successfully!").ok();
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from payment-service when refund to customer", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }
}
