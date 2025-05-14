package vn.ptit.moviebooking.payment.constants;

public interface RabbitMQConstants {

    interface Queue {
        String PAYMENT_PROCESS_COMMAND = "queue.payments.command";
        String PAYMENT_PROCESS_REPLY = "queue.payments.reply";
    }

    interface RoutingKey {
        String PAYMENT_PROCESS_COMMAND = "routingKey.payments.command";
        String PAYMENT_PROCESS_REPLY = "routingKey.payments.reply";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
