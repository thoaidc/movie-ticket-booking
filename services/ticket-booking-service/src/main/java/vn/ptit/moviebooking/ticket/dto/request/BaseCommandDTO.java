package vn.ptit.moviebooking.ticket.dto.request;

import java.io.Serial;
import java.io.Serializable;

@SuppressWarnings("unused")
public class BaseCommandDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int sagaId;

    public int getSagaId() {
        return sagaId;
    }

    public void setSagaId(int sagaId) {
        this.sagaId = sagaId;
    }
}
