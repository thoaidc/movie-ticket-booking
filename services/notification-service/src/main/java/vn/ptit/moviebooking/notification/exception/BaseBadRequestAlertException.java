package vn.ptit.moviebooking.notification.exception;

@SuppressWarnings("unused")
public class BaseBadRequestAlertException extends BaseException {

    public BaseBadRequestAlertException(String entityName, String message) {
        super(entityName, message, null);
    }

    public BaseBadRequestAlertException(String entityName, String message, Throwable error) {
        super(entityName, message, error);
    }
}
