package vn.ptit.moviebooking.payment.service.rabbitmq;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vn.ptit.moviebooking.payment.constants.RabbitMQConstants;
import vn.ptit.moviebooking.payment.exception.BaseBadRequestAlertException;

import java.io.IOException;

@Service
@SuppressWarnings("unused")
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final String ENTITY_NAME = "RabbitMQProducer";
    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String routingKey, Object message) {
        log.debug("Send message: {} - routing by key: {}", message, routingKey);
        rabbitTemplate.convertAndSend(RabbitMQConstants.Exchange.DIRECT_EXCHANGE, routingKey, message);
    }

    public void confirmProcessed(Channel channel, Message amqpMessage) {
        try {
            log.debug("Confirm message: {}", amqpMessage.getMessageProperties().getConsumerQueue());
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.error("[{}] Could not send confirm message to RabbitMQ. {}", ENTITY_NAME, e.getMessage());
            throw new BaseBadRequestAlertException(ENTITY_NAME, "Direct exchange not exists");
        }
    }

    public void notifyError(Channel channel, Message amqpMessage, boolean isRequeue) {
        try {
            String replyTo = amqpMessage.getMessageProperties().getReplyTo();
            log.debug("Send error to RabbitMQ: {}. Requeue: {}", replyTo, isRequeue);
            channel.basicNack(amqpMessage.getMessageProperties().getDeliveryTag(), false, isRequeue);
        } catch (IOException e) {
            log.error("[{}] - Failed to fallback to RabbitMQ. {}", ENTITY_NAME, e.getMessage());
        }
    }
}
