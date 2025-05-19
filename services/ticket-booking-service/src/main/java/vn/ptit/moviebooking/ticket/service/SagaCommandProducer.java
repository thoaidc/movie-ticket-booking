package vn.ptit.moviebooking.ticket.service;

import com.rabbitmq.client.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import vn.ptit.moviebooking.ticket.constants.BookingConstants;
import vn.ptit.moviebooking.ticket.constants.HttpStatusConstants;
import vn.ptit.moviebooking.ticket.constants.RabbitMQConstants;
import vn.ptit.moviebooking.ticket.constants.WebSocketConstants;
import vn.ptit.moviebooking.ticket.dto.request.BookingRequest;
import vn.ptit.moviebooking.ticket.dto.request.CheckSeatAvailabilityRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ConfirmSeatsRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.PaymentRequest;
import vn.ptit.moviebooking.ticket.dto.request.PaymentRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.RefundRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ReleasedSeatsRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.ValidateMovieRequestCommand;
import vn.ptit.moviebooking.ticket.dto.request.VerifyCustomerRequest;
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
    public SagaCommandProducer(RabbitMQProducer rabbitMQProducer,
                               TicketBookingService ticketBookingService,
                               WebSocketNotificationService notificationService) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.ticketBookingService = ticketBookingService;
        this.notificationService = notificationService;
    }

    public BaseResponseDTO createBookingSagaOrchestrator(BookingRequest bookingRequest) {
        // Create booking with PENDING status
        ValidateMovieRequestCommand command = ticketBookingService.createValidateMovieCommand(bookingRequest);
        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.MOVIE_VALIDATE_COMMAND, command);
        log.debug("[BOOKING] - Create booking with status PENDING and sagaId: {}", command.getSagaId());
        return BaseResponseDTO.builder().message("Create booking request successfully!").ok(command.getSagaId());
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.MOVIE_VALIDATE_REPLY)
    public void handleMovieValidateReply(BaseCommandReplyMessage replyMessage, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Invalid movie information! Cancel booking...")
                .build();

        try {
            // Movie validate successfully -> Check availability seats + Hold seats to payment
            if (replyMessage.isSuccess()) {
                CheckSeatAvailabilityRequestCommand command =
                        ticketBookingService.createCheckSeatsAvailabilityCommand(replyMessage);

                response = BaseResponseDTO.builder().message("Movie verified successfully!").ok();
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.MOVIE_VERIFIED);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.CHECK_SEATS_AVAILABILITY_COMMAND, command);
                log.debug("[BOOKING] - Movie information verified successfully");
            } else {
                // Movie validate failed -> Cancel booking
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.FAILED);
                log.debug("[BOOKING] - Movie information verified failed! Cancel booking.");
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from movie-service when validate movie", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.CHECK_SEATS_AVAILABILITY_REPLY)
    public void handleSeatsAvailabilityCheckingReply(BaseCommandReplyMessage replyMessage, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Seat held failed! Cancel booking...")
                .build();

        try {
            // Seats availability + Held seats successfully -> Verify customer + Save customer info
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Seats held successfully!").ok();
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.SEAT_RESERVED);
                log.debug("[BOOKING] - Seats availability! Seats held successfully!");
            } else {
                // Seats unavailability / Held seats failed -> Cancel booking
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.FAILED);
                log.debug("[BOOKING] - Seats unavailability or held failed! Cancel booking.");
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from seat-availability-service when checking seats availability", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    public BaseResponseDTO verifyCustomerInfo(VerifyCustomerRequest verifyCustomerRequest) {
        VerifyCustomerRequestCommand command = new VerifyCustomerRequestCommand();
        command.setSagaId(verifyCustomerRequest.getBookingId());
        command.setVerifyCustomerRequest(verifyCustomerRequest);
        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.VERIFY_CUSTOMER_COMMAND, command);
        return BaseResponseDTO.builder().ok();
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.VERIFY_CUSTOMER_REPLY)
    public void handleVerifyCustomerReply(BaseCommandReplyMessage replyMessage, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Your information verified failed! Cancel booking...")
                .build();

        try {
            // Customer verified successfully -> Payment process
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Your information verified successfully!").ok();
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.CUSTOMER_VERIFIED);
                ticketBookingService.updateBookingCustomerInfo(replyMessage);
                log.debug("[BOOKING] - Customer information verified successfully!");
            } else {
                // Verified failed -> Reserve held booking seats + Cancel booking
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.FAILED);
                ReleasedSeatsRequestCommand command = ticketBookingService.createReleasedBookingSeatsCommand(replyMessage);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.RELEASED_SEATS_COMMAND, command);
                log.debug("[BOOKING] - Customer information verified failed! Cancel booking.");
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from customer-service from verify customer information", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    public BaseResponseDTO payment(PaymentRequest paymentRequest) {
        PaymentRequestCommand command = new PaymentRequestCommand();
        command.setSagaId(paymentRequest.getBookingId());
        command.setPaymentRequest(paymentRequest);
        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.PAYMENT_PROCESS_COMMAND, command);
        return BaseResponseDTO.builder().ok();
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.PAYMENT_PROCESS_REPLY)
    public void handlePaymentProcessReply(BaseCommandReplyMessage replyMessage, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Payment failed! Cancel booking...")
                .build();

        try {
            // Payment successfully -> Confirm booking seats
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Payment successfully!").ok();
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.PAID);
                ConfirmSeatsRequestCommand command = ticketBookingService.createConfirmBookingSeatsCommand(replyMessage);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.CONFIRM_SEATS_COMMAND, command);
                log.debug("[BOOKING] - Payment successfully!");
            } else {
                // Payment failed -> Reserve held booking seats + Cancel booking
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.FAILED);
                ReleasedSeatsRequestCommand command = ticketBookingService.createReleasedBookingSeatsCommand(replyMessage);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.RELEASED_SEATS_COMMAND, command);
                log.debug("[BOOKING] - Payment failed! Cancel booking.");
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from payment-service when payment booking", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.CONFIRM_SEATS_REPLY)
    public void handleConfirmBookingSeatsReply(BaseCommandReplyMessage replyMessage, Channel channel, Message amqpMessage) {
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Confirmation of booked seat failed! Cancel booking...")
                .build();

        try {
            // Booked seats successfully -> Confirm order completed
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Booking successfully!").ok();
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.COMPLETED);
                log.debug("[BOOKING] - Confirm booked seats! Booking completed.");
            } else {
                // Booked seats failed -> Refund to customer + Reserve held booking seats + Cancel booking
                ticketBookingService.updateBookingStatus(replyMessage.getSagaId(), BookingConstants.Status.FAILED);
                ReleasedSeatsRequestCommand command = ticketBookingService.createReleasedBookingSeatsCommand(replyMessage);
                RefundRequestCommand refundCommand = new RefundRequestCommand();
                refundCommand.setSagaId(replyMessage.getSagaId());
                refundCommand.setReason("Confirmation of booked seat failed!");
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.RELEASED_SEATS_COMMAND, command);
                rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.PAYMENT_REFUND_COMMAND, refundCommand);
                log.debug("[BOOKING] - Confirm booked seats failed! Cancel booking.");
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from seat-availability-service when confirm booked seats", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.RELEASED_SEATS_REPLY)
    public void handleReserveSeatsReply(BaseCommandReplyMessage replyMessage, Channel channel, Message amqpMessage) {
        // When customer verified fail / Payment failed / Confirm booking seats failed
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Could not reserve booked seats!")
                .build();

        try {
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Reserve booked seats successfully!").ok();
                log.debug("[BOOKING] - Reserve booked seats successfully!");
            } else {
                log.debug("[BOOKING] - Reserve booked seats failed!");
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from seat-availability-service when reserve booked seats", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.PAYMENT_REFUND_REPLY)
    public void handleRefundReply(BaseCommandReplyMessage replyMessage, Channel channel, Message amqpMessage) {
        // When confirm booking seats failed
        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(HttpStatusConstants.BAD_REQUEST)
                .success(false)
                .message("Refund to customer failed!")
                .build();

        try {
            if (replyMessage.isSuccess()) {
                response = BaseResponseDTO.builder().message("Refund to customer successfully!").ok();
                log.debug("[BOOKING] - Refund to customer successfully!");
            } else {
                log.debug("[BOOKING] - Refund to customer failed!");
            }

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
            log.error("Could not execute response from payment-service when refund to customer", e);
        }

        notificationService.sendMessageToTopic(WebSocketConstants.Topic.BOOKING_TOPIC, response);
    }
}
