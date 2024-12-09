package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.enums.NotificationStatus;
import com.debttrack.platfrom.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatus(NotificationStatus status);
}