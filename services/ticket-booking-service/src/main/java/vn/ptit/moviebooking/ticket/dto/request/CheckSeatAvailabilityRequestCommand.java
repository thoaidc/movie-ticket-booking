package vn.ptit.moviebooking.ticket.dto.request;

public class CheckSeatAvailabilityRequestCommand extends BaseCommandDTO {

    private CheckSeatAvailabilityRequest checkSeatAvailabilityRequest;

    public CheckSeatAvailabilityRequest getCheckSeatAvailabilityRequest() {
        return checkSeatAvailabilityRequest;
    }

    public void setCheckSeatAvailabilityRequest(CheckSeatAvailabilityRequest checkSeatAvailabilityRequest) {
        this.checkSeatAvailabilityRequest = checkSeatAvailabilityRequest;
    }
}
