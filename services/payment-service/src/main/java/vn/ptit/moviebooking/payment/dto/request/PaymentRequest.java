package vn.ptit.moviebooking.payment.dto.request;

public class PaymentRequest {

    private Integer bookingId;
    private Float amount;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
