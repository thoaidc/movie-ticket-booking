package vn.ptit.moviebooking.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.moviebooking.notification.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {}
