package vn.ptit.moviebooking.notification.service.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import vn.ptit.moviebooking.notification.constants.RabbitMQConstants;
import vn.ptit.moviebooking.notification.service.NotificationService;

@Service
@DependsOn("bindingQueues")
public class SagaConsumer {

    private static final Logger log = LoggerFactory.getLogger(SagaConsumer.class);
    private static final String ENTITY_NAME = "SagaConsumer";
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

    @RabbitListener(queues = RabbitMQConstants.Queue.NOTIFICATION)
    public void handleNotificationRequest(String message, Channel channel, Message amqpMessage) {
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
