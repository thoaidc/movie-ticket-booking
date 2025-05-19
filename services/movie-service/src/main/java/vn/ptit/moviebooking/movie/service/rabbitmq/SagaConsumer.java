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
import vn.ptit.moviebooking.movie.dto.request.ValidateMovieCommand;
import vn.ptit.moviebooking.movie.dto.request.ValidateMovieRequest;
import vn.ptit.moviebooking.movie.dto.response.BaseCommandReplyMessage;
import vn.ptit.moviebooking.movie.dto.response.BaseResponseDTO;
import vn.ptit.moviebooking.movie.service.MovieService;

@Service
@DependsOn("bindingQueues")
public class SagaConsumer {

    private final RabbitMQProducer rabbitMQProducer;
    private final MovieService movieService;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(SagaConsumer.class);

    public SagaConsumer(RabbitMQProducer rabbitMQProducer, MovieService movieService, ObjectMapper objectMapper) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.movieService = movieService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.MOVIE_VALIDATE_COMMAND)
    public void handleValidateMovieInfoRequest(String message, Channel channel, Message amqpMessage) {
        log.debug("[Movie service] - Receive command: {}", message);
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage();

        try {
            ValidateMovieCommand command = objectMapper.readValue(message, ValidateMovieCommand.class);
            ValidateMovieRequest validateMovieRequest = command.getValidateMovieRequest();
            BaseResponseDTO response = movieService.validateMovieInfo(validateMovieRequest);

            replyMessage.setSagaId(command.getSagaId());
            replyMessage.setStatus(response.getStatus());

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.MOVIE_VALIDATE_REPLY, replyMessage);
    }
}
