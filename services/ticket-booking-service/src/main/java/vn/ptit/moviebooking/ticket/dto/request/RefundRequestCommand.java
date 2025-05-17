package vn.ptit.moviebooking.ticket.dto.request;

public class RefundRequestCommand extends BaseCommandDTO {

    private RefundRequest refundRequest;

    public RefundRequest getRefundRequest() {
        return refundRequest;
    }

    public void setRefundRequest(RefundRequest refundRequest) {
        this.refundRequest = refundRequest;
    }
}
