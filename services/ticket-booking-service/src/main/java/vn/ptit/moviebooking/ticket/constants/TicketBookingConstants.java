package vn.ptit.moviebooking.ticket.constants;

public interface TicketBookingConstants {

    interface BookingStatus {
        String PENDING = "PENDING";
        String SEAT_RESERVED = "SEAT_RESERVED";
        String PAID = "PAID";
        String CANCELLED = "CANCELLED";
        String FAILED = "FAILED";
        String COMPLETED = "COMPLETED";
    }
}
