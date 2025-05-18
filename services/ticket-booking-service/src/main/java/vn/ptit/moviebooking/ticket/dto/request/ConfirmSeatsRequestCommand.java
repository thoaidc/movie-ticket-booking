package vn.ptit.moviebooking.ticket.dto.request;

public class ConfirmSeatsRequestCommand extends BaseCommandDTO {

    private ConfirmSeatsRequest confirmSeatsRequest;

    public ConfirmSeatsRequest getConfirmSeatsRequest() {
        return confirmSeatsRequest;
    }

    public void setConfirmSeatsRequest(ConfirmSeatsRequest confirmSeatsRequest) {
        this.confirmSeatsRequest = confirmSeatsRequest;
    }
}
