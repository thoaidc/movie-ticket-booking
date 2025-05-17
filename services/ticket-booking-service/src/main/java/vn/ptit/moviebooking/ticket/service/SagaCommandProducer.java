package vn.ptit.moviebooking.ticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import vn.ptit.moviebooking.ticket.constants.RabbitMQConstants;
import vn.ptit.moviebooking.ticket.constants.TicketBookingConstants;
import vn.ptit.moviebooking.ticket.dto.request.BookingRequest;
import vn.ptit.moviebooking.ticket.dto.request.ValidateMovieRequest;
import vn.ptit.moviebooking.ticket.dto.request.ValidateMovieRequestCommand;
import vn.ptit.moviebooking.ticket.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.ticket.entity.Booking;
import vn.ptit.moviebooking.ticket.exception.BaseBadRequestAlertException;
import vn.ptit.moviebooking.ticket.service.rabbitmq.RabbitMQProducer;

@Service
@DependsOn("bindingQueues")
public class SagaCommandProducer {

    private static final Logger log = LoggerFactory.getLogger(SagaCommandProducer.class);
    private static final String ENTITY_NAME = "SagaCommandProducer";
    private final RabbitMQProducer rabbitMQProducer;
    private final TicketBookingService ticketBookingService;
    private final ObjectMapper objectMapper;

    public SagaCommandProducer(RabbitMQProducer rabbitMQProducer,
                               TicketBookingService ticketBookingService,
                               ObjectMapper objectMapper) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.ticketBookingService = ticketBookingService;
        this.objectMapper = objectMapper;
    }

    public BaseResponseDTO createBookingSagaOrchestrator(BookingRequest bookingRequest) {
        Booking booking = ticketBookingService.createBooking(bookingRequest); // Create booking with PENDING status
        ValidateMovieRequestCommand validateMovieCommand = new ValidateMovieRequestCommand();
        ValidateMovieRequest validateMovieRequest = new ValidateMovieRequest();
        validateMovieCommand.setSagaId(booking.getId());
        validateMovieRequest.setShowId(booking.getShowId());
        validateMovieRequest.setSeatIds(bookingRequest.getSeatIds());
        validateMovieRequest.setTotalAmount(booking.getTotalAmount());
        validateMovieCommand.setValidateMovieRequest(validateMovieRequest);

        try {
            String commandMessage = objectMapper.writeValueAsString(validateMovieCommand);
            rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.MOVIE_VALIDATE_COMMAND, commandMessage);
            return BaseResponseDTO.builder().ok("Create booking request successfully!");
        } catch (JsonProcessingException e) {
            ticketBookingService.updateBookingStatus(booking.getId(), TicketBookingConstants.BookingStatus.FAILED);
            throw new BaseBadRequestAlertException(ENTITY_NAME, "Could not create booking!");
        }
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.MOVIE_VALIDATE_REPLY)
    public void handleMovieValidateReply(String message, Channel channel, Message amqpMessage) {
        try {
            // Processed success -> send ack
            log.info("Received message from RabbitMQ: {}", message);
            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            // Notify error back to RabbitMQ with action requeue messages
            rabbitMQProducer.notifyError(channel, amqpMessage, true);
        }
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.CHECK_SEAT_AVAILABILITY_REPLY)
    public void handleSeatAvailabilityCheckingReply(String message, Channel channel, Message amqpMessage) {
        try {
            // Processed success -> send ack
            log.info("Received message from RabbitMQ: {}", message);
            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            // Notify error back to RabbitMQ with action requeue messages
            rabbitMQProducer.notifyError(channel, amqpMessage, true);
        }
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.VERIFY_CUSTOMER_REPLY)
    public void handleCustomerVerifyReply(String message, Channel channel, Message amqpMessage) {
        try {
            // Processed success -> send ack
            log.info("Received message from RabbitMQ: {}", message);
            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            // Notify error back to RabbitMQ with action requeue messages
            rabbitMQProducer.notifyError(channel, amqpMessage, true);
        }
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.PAYMENT_PROCESS_REPLY)
    public void handlePaymentProcessReply(String message, Channel channel, Message amqpMessage) {
        try {
            // Processed success -> send ack
            log.info("Received message from RabbitMQ: {}", message);
            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            // Notify error back to RabbitMQ with action requeue messages
            rabbitMQProducer.notifyError(channel, amqpMessage, true);
        }
    }
}
