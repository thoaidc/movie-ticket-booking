package vn.ptit.moviebooking.notification.service.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import vn.ptit.moviebooking.notification.constants.NotificationConstants;
import vn.ptit.moviebooking.notification.constants.RabbitMQConstants;
import vn.ptit.moviebooking.notification.dto.request.NotificationProcessCommand;
import vn.ptit.moviebooking.notification.dto.request.NotificationRequest;
import vn.ptit.moviebooking.notification.dto.response.BaseCommandReplyMessage;
import vn.ptit.moviebooking.notification.entity.Notification;
import vn.ptit.moviebooking.notification.service.NotificationService;

import java.util.Objects;

@Service
@DependsOn("bindingQueues")
public class SagaConsumer {

    private static final Logger log = LoggerFactory.getLogger(SagaConsumer.class);
    private final NotificationService notificationService;
    private final RabbitMQProducer rabbitMQProducer;
    private final ObjectMapper objectMapper;

    public SagaConsumer(NotificationService notificationService,
                        RabbitMQProducer rabbitMQProducer,
                        ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.rabbitMQProducer = rabbitMQProducer;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.NOTIFICATION_COMMAND)
    public void handleNotificationRequest(String message, Channel channel, Message amqpMessage) {
        log.info("Received message from RabbitMQ: {}", message);
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage();
        String replyMessageStr = null;

        try {
            NotificationProcessCommand command = objectMapper.convertValue(message, NotificationProcessCommand.class);
            NotificationRequest notificationRequest = command.getNotificationRequest();
            Notification notification = notificationService.sendNotification(notificationRequest);

            boolean isSentSuccess = Objects.equals(NotificationConstants.NotificationStatus.SUCCESS, notification.getStatus());
            replyMessage.setStatus(isSentSuccess);
            replyMessage.setResult(notification);
            replyMessage.setSagaId(command.getSagaId());
            replyMessageStr = objectMapper.writeValueAsString(replyMessage);

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.NOTIFICATION_REPLY, replyMessageStr);
    }
}
