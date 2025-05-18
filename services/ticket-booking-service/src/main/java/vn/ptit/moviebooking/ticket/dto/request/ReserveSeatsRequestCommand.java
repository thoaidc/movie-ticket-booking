package vn.ptit.moviebooking.ticket.dto.request;

public class ReserveSeatsRequestCommand extends BaseCommandDTO {

    private ReserveSeatsRequest reserveSeatsRequest;

    public ReserveSeatsRequest getReserveSeatsRequest() {
        return reserveSeatsRequest;
    }

    public void setReserveSeatsRequest(ReserveSeatsRequest reserveSeatsRequest) {
        this.reserveSeatsRequest = reserveSeatsRequest;
    }
}
