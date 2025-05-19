package vn.ptit.moviebooking.ticket.constants;

public interface BookingConstants {

    interface Status {
        String PENDING = "PENDING";
        String MOVIE_VERIFIED = "MOVIE_VERIFIED";
        String SEAT_RESERVED = "SEAT_RESERVED";
        String CUSTOMER_VERIFIED = "CUSTOMER_VERIFIED";
        String PAID = "PAID";
        String CANCELLED = "CANCELLED";
        String FAILED = "FAILED";
        String COMPLETED = "COMPLETED";
    }
}
