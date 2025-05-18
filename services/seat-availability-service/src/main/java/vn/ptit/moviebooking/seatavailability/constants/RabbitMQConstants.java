package vn.ptit.moviebooking.seatavailability.constants;

public interface RabbitMQConstants {

    interface Queue {
        String CHECK_SEATS_AVAILABILITY_COMMAND = "queue.seats.availability.command";
        String CONFIRM_SEATS_COMMAND = "queue.seats.confirm.command";
        String RESERVE_SEATS_COMMAND = "queue.seats.released.command";
    }

    interface RoutingKey {
        String CHECK_SEATS_AVAILABILITY_REPLY = "queue.seats.availability.reply";
        String CONFIRM_SEATS_REPLY = "queue.seats.confirm.reply";
        String RELEASED_SEATS_REPLY = "queue.seats.released.reply";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
