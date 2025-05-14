package vn.ptit.moviebooking.notification.constants;

public interface RabbitMQConstants {

    interface Queue {
        String NOTIFICATION = "queue.notifications";
    }

    interface RoutingKey {
        String NOTIFICATION = "routingKey.notifications";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
