package vn.ptit.moviebooking.customer.dto.request;

public class VerifyCustomerCommand {

    private int sagaId;
    private SaveCustomerRequest saveCustomerRequest;

    public int getSagaId() {
        return sagaId;
    }

    public void setSagaId(int sagaId) {
        this.sagaId = sagaId;
    }

    public SaveCustomerRequest getSaveCustomerRequest() {
        return saveCustomerRequest;
    }

    public void setSaveCustomerRequest(SaveCustomerRequest saveCustomerRequest) {
        this.saveCustomerRequest = saveCustomerRequest;
    }
}
