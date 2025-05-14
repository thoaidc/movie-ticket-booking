package vn.ptit.moviebooking.customer.constants;

public interface RabbitMQConstants {

    interface Queue {
        String VERIFY_CUSTOMER_COMMAND = "queue.customers.command";
    }

    interface RoutingKey {
        String VERIFY_CUSTOMER_REPLY = "routingKey.customers.reply";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
