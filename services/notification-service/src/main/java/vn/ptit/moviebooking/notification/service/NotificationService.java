package vn.ptit.moviebooking.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ptit.moviebooking.notification.constants.NotificationConstants;
import vn.ptit.moviebooking.notification.dto.request.NotificationRequest;
import vn.ptit.moviebooking.notification.entity.Notification;
import vn.ptit.moviebooking.notification.repository.NotificationRepository;

import java.time.Instant;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Notification sendNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        notification.setReceiver(notificationRequest.getReceiver());
        notification.setSender(notificationRequest.getSender());
        notification.setTitle(notificationRequest.getTitle());
        notification.setContent(notificationRequest.getContent());
        notification.setStatus(NotificationConstants.NotificationStatus.PROCESSING);
        notification.setSentAt(Instant.now());
        notificationRepository.save(notification);

        return sendNotificationToClient(notification);
    }

    @Transactional
    public Notification sendNotificationToClient(Notification notification) {
        notification.setStatus(NotificationConstants.NotificationStatus.SUCCESS);
        return notificationRepository.save(notification);
    }
}
