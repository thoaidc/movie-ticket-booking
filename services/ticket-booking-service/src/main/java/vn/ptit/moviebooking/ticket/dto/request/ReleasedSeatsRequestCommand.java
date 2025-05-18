package vn.ptit.moviebooking.ticket.dto.request;

public class ReleasedSeatsRequestCommand extends BaseCommandDTO {

    private ReleasedSeatsRequest releasedSeatsRequest;

    public ReleasedSeatsRequest getReleasedSeatsRequest() {
        return releasedSeatsRequest;
    }

    public void setReleasedSeatsRequest(ReleasedSeatsRequest releasedSeatsRequest) {
        this.releasedSeatsRequest = releasedSeatsRequest;
    }
}
