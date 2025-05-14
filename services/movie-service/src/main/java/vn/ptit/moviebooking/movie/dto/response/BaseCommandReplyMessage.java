package vn.ptit.moviebooking.movie.dto.response;

public class BaseCommandReplyMessage {

    private boolean status;
    private Object result;

    public BaseCommandReplyMessage(boolean status, Object result) {
        this.status = status;
        this.result = result;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
