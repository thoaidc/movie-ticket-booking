package vn.ptit.moviebooking.movie.service.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import vn.ptit.moviebooking.movie.constants.RabbitMQConstants;
import vn.ptit.moviebooking.movie.service.MovieService;

@Service
@DependsOn("bindingQueues")
public class SagaConsumer {

    private static final Logger log = LoggerFactory.getLogger(SagaConsumer.class);
    private static final String ENTITY_NAME = "SagaConsumer";
    private final RabbitMQProducer rabbitMQProducer;
    private final MovieService movieService;
    private final ObjectMapper objectMapper;

    public SagaConsumer(RabbitMQProducer rabbitMQProducer, MovieService movieService, ObjectMapper objectMapper) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.movieService = movieService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.MOVIE_VALIDATE_COMMAND)
    public void handleValidateMovieInfoRequest(String message, Channel channel, Message amqpMessage) {
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
