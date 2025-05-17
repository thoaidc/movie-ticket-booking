package vn.ptit.moviebooking.payment.constants;

public interface PaymentConstants {

    interface PaymentStatus {
        String PENDING = "PENDING";
        String COMPLETED = "COMPLETED";
        String FAILED = "FAILED";
        String REFUNDED = "REFUNDED";
    }
}
