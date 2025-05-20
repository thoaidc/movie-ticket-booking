package vn.ptit.moviebooking.payment.service.rabbitmq;

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

    public SagaConsumer(PaymentService paymentService,
                        RabbitMQProducer rabbitMQProducer) {
        this.paymentService = paymentService;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.PAYMENT_PROCESS_COMMAND)
    public void handlePaymentProcessRequest(PaymentProcessCommand command, Channel channel, Message amqpMessage) {
        log.info("[BOOKING] - Payment process: {}", command);
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage();
        replyMessage.setSagaId(command.getSagaId());

        try {
            PaymentRequest paymentRequest = command.getPaymentRequest();
            Payment payment = paymentService.createPayment(paymentRequest);
            payment = paymentService.paymentProcessTest(payment, paymentRequest);

            boolean isPaymentSuccess = Objects.equals(PaymentConstants.PaymentStatus.COMPLETED, payment.getStatus());
            replyMessage.setStatus(isPaymentSuccess);
            replyMessage.setResult(payment);
            log.info("[BOOKING] - Payment process status: {}", isPaymentSuccess);

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.PAYMENT_PROCESS_REPLY, replyMessage);
    }

    @RabbitListener(queues = RabbitMQConstants.Queue.PAYMENT_REFUND_COMMAND)
    public void handleRefundRequest(RefundProcessCommand command, Channel channel, Message amqpMessage) {
        log.info("[BOOKING] - Refund process: {}", command);
        BaseCommandReplyMessage replyMessage = new BaseCommandReplyMessage();
        replyMessage.setSagaId(command.getSagaId());

        try {
            Refund refund = paymentService.refund(command);
            replyMessage.setStatus(true);
            replyMessage.setResult(refund);

            rabbitMQProducer.confirmProcessed(channel, amqpMessage);
        } catch (Exception e) {
            rabbitMQProducer.notifyError(channel, amqpMessage, false);
        }

        rabbitMQProducer.sendMessage(RabbitMQConstants.RoutingKey.PAYMENT_REFUND_REPLY, replyMessage);
    }
}
