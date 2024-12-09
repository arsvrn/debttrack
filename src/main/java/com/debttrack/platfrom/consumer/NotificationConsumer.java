package com.debttrack.platfrom.consumer;

import com.debttrack.platfrom.config.RabbitMQConfig;
import com.debttrack.platfrom.model.Notification;
import com.debttrack.platfrom.repository.NotificationRepository;
import com.debttrack.platfrom.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationService.sendNotification(notification);
    }
}