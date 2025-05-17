package vn.ptit.moviebooking.payment.dto.request;

public class RefundProcessCommand {

    private int sagaId;
    private RefundRequest refundRequest;

    public int getSagaId() {
        return sagaId;
    }

    public void setSagaId(int sagaId) {
        this.sagaId = sagaId;
    }

    public RefundRequest getRefundRequest() {
        return refundRequest;
    }

    public void setRefundRequest(RefundRequest refundRequest) {
        this.refundRequest = refundRequest;
    }
}
