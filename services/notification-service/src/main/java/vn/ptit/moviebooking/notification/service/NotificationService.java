package vn.ptit.moviebooking.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ptit.moviebooking.notification.constants.NotificationConstants;
import vn.ptit.moviebooking.notification.dto.request.NotificationRequest;
import vn.ptit.moviebooking.notification.entity.Notification;
import vn.ptit.moviebooking.notification.repository.NotificationRepository;

import java.time.Instant;
import java.util.Objects;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Notification sendNotification(NotificationRequest notificationRequest) {
        if (Objects.isNull(notificationRequest)) {
            return null;
        }

        Notification notification = new Notification();
        notification.setReceiver(notificationRequest.getReceiver());
        notification.setSender(notificationRequest.getSender());
        notification.setTitle(notificationRequest.getTitle());
        notification.setContent(notificationRequest.getContent());
        notification.setStatus(NotificationConstants.NotificationStatus.PROCESSING);
        notification.setSentAt(Instant.now());
        return sendNotificationToClient(notificationRepository.save(notification));
    }

    @Transactional
    public Notification sendNotificationToClient(Notification notification) {
        notification.setStatus(NotificationConstants.NotificationStatus.SUCCESS);
        return notificationRepository.save(notification);
    }
}
