package com.debttrack.platfrom.service;

import com.debttrack.platfrom.enums.NotificationStatus;
import com.debttrack.platfrom.model.Notification;
import com.debttrack.platfrom.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    public void sendNotification(Notification notification) {
        try {
            // Пример отправки email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getUser().getEmail());
            message.setSubject("Напоминание о долге");
            message.setText(notification.getMessage());
            mailSender.send(message);

            // Обновить статус уведомления
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
        } catch (Exception e) {
            e.printStackTrace(); // Логировать ошибки
        }
    }

    public void sendPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository.findByStatus(NotificationStatus.PENDING);
        for (Notification notification : pendingNotifications) {
            sendNotification(notification);
        }
    }
}