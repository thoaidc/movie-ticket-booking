package vn.ptit.moviebooking.payment.service.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import vn.ptit.moviebooking.payment.constants.PaymentConstants;
import vn.ptit.moviebooking.payment.constants.RabbitMQConstants;
import vn.ptit.moviebooking.payment.dto.request.PaymentProcessCommand;
import vn.ptit.moviebooking.payment.dto.request.PaymentRequest;
import vn.ptit.moviebooking.payment.dto.request.RefundProcessCommand;
import vn.ptit.moviebooking.payment.dto.request.RefundRequest;
import vn.ptit.moviebooking.payment.dto.response.BaseCommandReplyMessage;
import vn.ptit.moviebooking.payment.entity.Payment;
import vn.ptit.moviebooking.payment.entity.Refund;
import vn.ptit.moviebooking.payment.service.PaymentService;

import java.util.Objects;

@Service
@DependsOn("bindingQueues")
public class SagaConsumer {

    private static final Logger log = LoggerFactory.getLogger(SagaConsumer.class);
    private final PaymentService paymentService;
    private final RabbitMQProducer rabbitMQProducer;
    private final ObjectMapper objectMapper;

    public SagaConsumer(PaymentService paymentService,
                        RabbitMQProducer rabbitMQProducer,
                        ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.rabbitMQProducer = rabbitMQProducer;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.PAYMENT_PROCESS_COMMAND)
    public void handlePaymentProcessRequest(String message, Channel channel, Message amqpMessage) {
        log.info("Received message from RabbitMQ: {}", message);
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage(false, null);
        String replyMessageStr = null;

        try {
            PaymentProcessCommand command = objectMapper.convertValue(message, PaymentProcessCommand.class);
            PaymentRequest paymentRequest = command.getPaymentRequest();

            Payment payment = paymentService.createPayment(paymentRequest);
            payment = paymentService.paymentProcessTest(payment);

            boolean isPaymentSuccess = Objects.equals(PaymentConstants.PaymentStatus.COMPLETED, payment.getStatus());
            replyMessage.setStatus(isPaymentSuccess);
            replyMessage.setResult(payment);
            replyMessageStr = objectMapper.writeValueAsString(replyMessage);

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.PAYMENT_PROCESS_REPLY, replyMessageStr);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.PAYMENT_REFUND_COMMAND)
    public void handleRefundRequest(String message, Channel channel, Message amqpMessage) {
        log.info("Received message from RabbitMQ: {}", message);
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage(false, null);
        String replyMessageStr = null;

        try {
            RefundProcessCommand command = objectMapper.convertValue(message, RefundProcessCommand.class);
            RefundRequest refundRequest = command.getRefundRequest();
            Refund refund = paymentService.refund(refundRequest);

            replyMessage.setStatus(true);
            replyMessage.setResult(refund);
            replyMessageStr = objectMapper.writeValueAsString(replyMessage);

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.PAYMENT_REFUND_REPLY, replyMessageStr);
    }
}
