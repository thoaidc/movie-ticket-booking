package vn.ptit.moviebooking.payment.dto.request;

public class RefundProcessCommand {

    private int sagaId;
    private String reason;

    public int getSagaId() {
        return sagaId;
    }

    public void setSagaId(int sagaId) {
        this.sagaId = sagaId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
