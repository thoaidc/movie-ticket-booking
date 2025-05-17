package vn.ptit.moviebooking.notification.dto.request;

public class NotificationProcessCommand {

    private int sagaId;
    private NotificationRequest notificationRequest;

    public int getSagaId() {
        return sagaId;
    }

    public void setSagaId(int sagaId) {
        this.sagaId = sagaId;
    }

    public NotificationRequest getNotificationRequest() {
        return notificationRequest;
    }

    public void setNotificationRequest(NotificationRequest notificationRequest) {
        this.notificationRequest = notificationRequest;
    }
}
