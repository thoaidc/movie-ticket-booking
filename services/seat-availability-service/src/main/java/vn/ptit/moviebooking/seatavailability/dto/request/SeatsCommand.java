package vn.ptit.moviebooking.seatavailability.dto.request;

import java.util.List;

public class SeatsCommand {

    private int sagaId;
    private List<Integer> seatIds;

    public int getSagaId() {
        return sagaId;
    }

    public void setSagaId(int sagaId) {
        this.sagaId = sagaId;
    }

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }
}
