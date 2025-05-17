package vn.ptit.moviebooking.payment.constants;

public interface RabbitMQConstants {

    interface Queue {
        String PAYMENT_PROCESS_COMMAND = "queue.payments.process.command";
        String PAYMENT_REFUND_COMMAND = "queue.payments.refund.command";
    }

    interface RoutingKey {
        String PAYMENT_PROCESS_REPLY = "routingKey.payments.process.reply";
        String PAYMENT_REFUND_REPLY = "routingKey.payments.refund.reply";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
