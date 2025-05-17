package vn.ptit.moviebooking.seatavailability.constants;

public interface RabbitMQConstants {

    interface Queue {
        String CHECK_SEAT_AVAILABILITY_COMMAND = "queue.seats.availability.command";
        String CONFIRM_SEAT_COMMAND = "queue.seats.confirm.command";
        String RESERVE_SEAT_COMMAND = "queue.seats.reserve.command";
    }

    interface RoutingKey {
        String CHECK_SEAT_AVAILABILITY_REPLY = "queue.seats.availability.reply";
        String CONFIRM_SEAT_REPLY = "queue.seats.confirm.reply";
        String RESERVE_SEAT_REPLY = "queue.seats.reserve.reply";
    }

    interface Exchange {
        String DIRECT_EXCHANGE = "amq.direct";
    }
}
