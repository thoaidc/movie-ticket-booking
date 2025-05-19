package vn.ptit.moviebooking.seatavailability.service.rabbitmq;

import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import vn.ptit.moviebooking.seatavailability.constants.RabbitMQConstants;
import vn.ptit.moviebooking.seatavailability.dto.request.SeatsCommand;
import vn.ptit.moviebooking.seatavailability.dto.response.BaseCommandReplyMessage;
import vn.ptit.moviebooking.seatavailability.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.seatavailability.service.CheckSeatAvailabilityService;

@Service
@DependsOn("bindingQueues")
public class SagaConsumer {

    private final RabbitMQProducer rabbitMQProducer;
    private final CheckSeatAvailabilityService checkSeatAvailabilityService;

    public SagaConsumer(RabbitMQProducer rabbitMQProducer,
                        CheckSeatAvailabilityService checkSeatAvailabilityService) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.checkSeatAvailabilityService = checkSeatAvailabilityService;
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.CHECK_SEATS_AVAILABILITY_COMMAND)
    public void handleCheckSeatAvailabilityRequest(SeatsCommand command, Channel channel, Message amqpMessage) {
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage();

        try {
            BaseResponseDTO response = checkSeatAvailabilityService.checkSeatsAvailability(command);
            replyMessage.setSagaId(command.getSagaId());
            replyMessage.setStatus(response.getStatus());
            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.CHECK_SEATS_AVAILABILITY_REPLY, replyMessage);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.CONFIRM_SEATS_COMMAND)
    public void handleConfirmBookedSeatsRequest(SeatsCommand command, Channel channel, Message amqpMessage) {
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage();

        try {
            BaseResponseDTO response = checkSeatAvailabilityService.confirmBookedSeats(command);
            replyMessage.setSagaId(command.getSagaId());
            replyMessage.setStatus(response.getStatus());
            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.CONFIRM_SEATS_REPLY, replyMessage);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.RELEASED_SEATS_COMMAND)
    public void handleReleaseBookedSeatsRequest(SeatsCommand command, Channel channel, Message amqpMessage) {
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage();

        try {
            BaseResponseDTO response = checkSeatAvailabilityService.releasedBookedSeats(command);
            replyMessage.setSagaId(command.getSagaId());
            replyMessage.setStatus(response.getStatus());
            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.RELEASED_SEATS_REPLY, replyMessage);
    }
}
