package vn.ptit.moviebooking.ticket.constants;

public interface RabbitMQConstants {

    interface Queue {

        String MOVIE_VALIDATE_COMMAND = "queue.movies.command";
        String MOVIE_VALIDATE_REPLY = "queue.movies.reply";
        String CHECK_SEAT_AVAILABILITY_COMMAND = "queue.seats.command";
        String CHECK_SEAT_AVAILABILITY_REPLY = "queue.seats.reply";
        String VERIFY_CUSTOMER_COMMAND = "queue.customers.command";
        String VERIFY_CUSTOMER_REPLY = "queue.customer.reply";
        String PAYMENT_PROCESS_COMMAND = "queue.payments.command";
        String PAYMENT_PROCESS_REPLY = "queue.payments.reply";
        String NOTIFICATION = "queue.notifications";
    }

    interface RoutingKey {
        String MOVIE_VALIDATE_COMMAND = "routingKey.movies.command";
        String MOVIE_VALIDATE_REPLY = "routingKey.movies.reply";
        String CHECK_SEAT_AVAILABILITY_COMMAND = "routingKey.seats.command";
        String CHECK_SEAT_AVAILABILITY_REPLY = "routingKey.seats.reply";
        String VERIFY_CUSTOMER_COMMAND = "routingKey.customers.command";
        String VERIFY_CUSTOMER_REPLY = "routingKey.customer.reply";
        String PAYMENT_PROCESS_COMMAND = "routingKey.payments.command";
        String PAYMENT_PROCESS_REPLY = "routingKey.payments.reply";
        String NOTIFICATION = "routingKey.notifications";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
