package vn.ptit.moviebooking.movie.dto.response;

import java.util.List;

public class ShowSeatResponse {

    private Integer showId;
    private List<Integer> seatIds;

    public ShowSeatResponse() {}

    public ShowSeatResponse(Integer showId, List<Integer> seatIds) {
        this.showId = showId;
        this.seatIds = seatIds;
    }

    public Integer getShowId() {
        return showId;
    }

    public void setShowId(Integer showId) {
        this.showId = showId;
    }

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }
}
