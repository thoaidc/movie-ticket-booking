package vn.ptit.moviebooking.ticket.dto.request;

public class NotificationCommand extends BaseCommandDTO {

    private NotificationRequest notificationRequest;

    public NotificationRequest getNotificationRequest() {
        return notificationRequest;
    }

    public void setNotificationRequest(NotificationRequest notificationRequest) {
        this.notificationRequest = notificationRequest;
    }
}
