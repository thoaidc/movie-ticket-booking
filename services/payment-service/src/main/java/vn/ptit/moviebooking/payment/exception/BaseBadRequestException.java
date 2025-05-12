package vn.ptit.moviebooking.payment.exception;

@SuppressWarnings("unused")
public class BaseBadRequestException extends BaseException {

    public BaseBadRequestException(String entityName, String message) {
        super(entityName, message, null);
    }

    public BaseBadRequestException(String entityName, String message, Throwable error) {
        super(entityName, message, error);
    }
}
