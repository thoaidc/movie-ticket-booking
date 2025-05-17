package vn.ptit.moviebooking.notification.constants;

public interface RabbitMQConstants {

    interface Queue {
        String NOTIFICATION_COMMAND = "queue.notifications.command";
    }

    interface RoutingKey {
        String NOTIFICATION_REPLY = "routingKey.notifications.reply";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
