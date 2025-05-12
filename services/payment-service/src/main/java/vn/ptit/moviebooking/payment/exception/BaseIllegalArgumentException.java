package vn.ptit.moviebooking.payment.exception;

@SuppressWarnings("unused")
public class BaseIllegalArgumentException extends BaseException {

    public BaseIllegalArgumentException(String entityName, String message) {
        super(entityName, message, null);
    }

    public BaseIllegalArgumentException(String entityName, String message, Throwable error) {
        super(entityName, message, error);
    }
}
