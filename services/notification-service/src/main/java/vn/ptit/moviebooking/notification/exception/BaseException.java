package vn.ptit.moviebooking.notification.exception;

@SuppressWarnings("unused")
public abstract class BaseException extends RuntimeException {

    private final String entityName;
    private final String message;
    private final Throwable error;

    protected BaseException(String entityName, String message, Throwable error) {
        super(entityName + " - " + message, error);
        this.entityName = entityName;
        this.message = message;
        this.error = error;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getError() {
        return error;
    }
}
