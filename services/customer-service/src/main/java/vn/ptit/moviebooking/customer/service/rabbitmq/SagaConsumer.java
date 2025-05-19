package vn.ptit.moviebooking.customer.service.rabbitmq;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import vn.ptit.moviebooking.customer.constants.RabbitMQConstants;
import vn.ptit.moviebooking.customer.dto.request.VerifyCustomerRequest;
import vn.ptit.moviebooking.customer.dto.request.VerifyCustomerRequestCommand;
import vn.ptit.moviebooking.customer.dto.response.BaseCommandReplyMessage;
import vn.ptit.moviebooking.customer.entity.Customer;
import vn.ptit.moviebooking.customer.service.CustomerService;

import java.util.Objects;

@Service
@DependsOn("bindingQueues")
public class SagaConsumer {

    private static final Logger log = LoggerFactory.getLogger(SagaConsumer.class);
    private final RabbitMQProducer rabbitMQProducer;
    private final CustomerService customerService;

    public SagaConsumer(RabbitMQProducer rabbitMQProducer,
                        CustomerService customerService) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.customerService = customerService;
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.VERIFY_CUSTOMER_COMMAND)
    public void handleVerifyCustomerRequest(VerifyCustomerRequestCommand command, Channel channel, Message amqpMessage) {
        log.info("Received message from RabbitMQ: {}", command);
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage();

        try {
            VerifyCustomerRequest saveCustomerRequest = command.getVerifyCustomerRequest();
            Customer customer = customerService.saveCustomerInfo(saveCustomerRequest);

            replyMessage.setSagaId(command.getSagaId());
            replyMessage.setStatus(Objects.nonNull(customer) && customer.getId() > 0);
            replyMessage.setResult(customer.getId());

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            log.error("Could process verify customer request", e);
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        // Reply result to saga orchestrator in Ticket booking service
        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.VERIFY_CUSTOMER_REPLY, replyMessage);
    }
}
