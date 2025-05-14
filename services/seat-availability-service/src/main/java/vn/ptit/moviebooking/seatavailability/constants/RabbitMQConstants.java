package vn.ptit.moviebooking.seatavailability.constants;

public interface RabbitMQConstants {

    interface Queue {
        String CHECK_SEAT_AVAILABILITY_COMMAND = "queue.seats.command";
        String CHECK_SEAT_AVAILABILITY_REPLY = "queue.seats.reply";
    }

    interface RoutingKey {
        String CHECK_SEAT_AVAILABILITY_COMMAND = "routingKey.seats.command";
        String CHECK_SEAT_AVAILABILITY_REPLY = "routingKey.seats.reply";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
