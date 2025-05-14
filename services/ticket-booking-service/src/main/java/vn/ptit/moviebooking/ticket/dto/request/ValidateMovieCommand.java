package vn.ptit.moviebooking.ticket.dto.request;

import java.util.List;

public class ValidateMovieCommand extends BaseCommandDTO {

    private Integer showId;
    private List<Integer> seatIds;
    private Float totalAmount;

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

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
