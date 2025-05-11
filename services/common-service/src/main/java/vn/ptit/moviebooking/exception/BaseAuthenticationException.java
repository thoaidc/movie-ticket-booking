package vn.ptit.moviebooking.exception;

@SuppressWarnings("unused")
public class BaseAuthenticationException extends BaseException {

    public BaseAuthenticationException(String entityName, String message) {
        super(entityName, message, null);
    }

    public BaseAuthenticationException(String entityName, String message, Throwable error) {
        super(entityName, message, error);
    }
}
