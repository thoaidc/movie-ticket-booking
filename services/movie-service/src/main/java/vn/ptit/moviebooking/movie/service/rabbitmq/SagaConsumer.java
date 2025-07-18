package vn.ptit.moviebooking.movie.service.rabbitmq;

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
    private static final Logger log = LoggerFactory.getLogger(SagaConsumer.class);

    public SagaConsumer(RabbitMQProducer rabbitMQProducer, MovieService movieService) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.movieService = movieService;
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.MOVIE_VALIDATE_COMMAND)
    public void handleValidateMovieInfoRequest(ValidateMovieCommand command, Channel channel, Message amqpMessage) {
        log.info("[BOOKING] - Validate movie info: {}", command);
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage();
        replyMessage.setSagaId(command.getSagaId());

        try {
            ValidateMovieRequest validateMovieRequest = command.getValidateMovieRequest();
            BaseResponseDTO response = movieService.validateMovieInfo(validateMovieRequest);
            replyMessage.setStatus(response.getStatus());
            log.info("[BOOKING] - Validate movie status: {}", replyMessage.getStatus());

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.MOVIE_VALIDATE_REPLY, replyMessage);
    }
}
