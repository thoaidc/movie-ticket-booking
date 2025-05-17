package vn.ptit.moviebooking.ticket.dto.request;

import java.util.List;

public class CheckSeatAvailabilityRequest {

    private List<Integer> seatIds;

    private Integer showId;

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }

    public Integer getShowId() {
        return showId;
    }

    public void setShowId(Integer showId) {
        this.showId = showId;
    }
}
