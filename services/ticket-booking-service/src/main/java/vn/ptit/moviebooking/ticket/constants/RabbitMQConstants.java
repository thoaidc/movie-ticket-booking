package vn.ptit.moviebooking.ticket.constants;

public interface RabbitMQConstants {

    interface Queue {
        String MOVIE_VALIDATE_REPLY = "queue.movies.reply";
        String CHECK_SEATS_AVAILABILITY_REPLY = "queue.seats.availability.reply";
        String CONFIRM_SEATS_REPLY = "queue.seats.confirm.reply";
        String RELEASED_SEATS_REPLY = "queue.seats.released.reply";
        String VERIFY_CUSTOMER_REPLY = "queue.customers.reply";
        String PAYMENT_PROCESS_REPLY = "queue.payments.process.reply";
        String PAYMENT_REFUND_REPLY = "queue.payments.refund.reply";
        String NOTIFICATION_REPLY = "queue.notifications.reply";
    }

    interface RoutingKey {
        String MOVIE_VALIDATE_COMMAND = "routingKey.movies.command";
        String CHECK_SEATS_AVAILABILITY_COMMAND = "routingKey.seats.availability.command";
        String CONFIRM_SEATS_COMMAND = "routingKey.seats.confirm.command";
        String RELEASED_SEATS_COMMAND = "routingKey.seats.released.command";
        String VERIFY_CUSTOMER_COMMAND = "routingKey.customers.command";
        String PAYMENT_PROCESS_COMMAND = "routingKey.payments.process.command";
        String PAYMENT_REFUND_COMMAND = "routingKey.payments.refund.command";
        String NOTIFICATION_COMMAND = "routingKey.notifications.command";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
