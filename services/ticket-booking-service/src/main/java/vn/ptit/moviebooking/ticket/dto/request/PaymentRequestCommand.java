package vn.ptit.moviebooking.ticket.dto.request;

public class PaymentRequestCommand extends BaseCommandDTO {

    private PaymentRequest paymentRequest;

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }
}
