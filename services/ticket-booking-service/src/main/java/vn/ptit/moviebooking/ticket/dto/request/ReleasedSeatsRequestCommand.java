package vn.ptit.moviebooking.ticket.dto.request;

import java.util.List;

public class ReleasedSeatsRequestCommand extends BaseCommandDTO {

    private List<Integer> seatIds;

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }
}
