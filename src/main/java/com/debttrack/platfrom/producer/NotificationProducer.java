package com.debttrack.platfrom.producer;

import com.debttrack.platfrom.config.RabbitMQConfig;
import com.debttrack.platfrom.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendToQueue(Notification notification) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, notification.getId());
    }
}