package com.debttrack.platfrom.producer;

import com.debttrack.platfrom.config.RabbitMQConfig;
import com.debttrack.platfrom.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class NotificationProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private NotificationProducer notificationProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendToQueue() {
        Notification notification = new Notification();
        notification.setId(1L);

        notificationProducer.sendToQueue(notification);

        verify(rabbitTemplate, times(1)).convertAndSend(RabbitMQConfig.QUEUE_NAME, notification.getId());
    }
}
