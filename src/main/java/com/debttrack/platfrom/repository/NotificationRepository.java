package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.enums.NotificationStatus;
import com.debttrack.platfrom.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatus(NotificationStatus status);
}