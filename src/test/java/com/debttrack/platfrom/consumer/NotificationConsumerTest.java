package com.debttrack.platfrom.consumer;

import com.debttrack.platfrom.model.Notification;
import com.debttrack.platfrom.repository.NotificationRepository;
import com.debttrack.platfrom.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NotificationConsumerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessNotification_Success() {
        Long notificationId = 1L;
        Notification notification = new Notification();
        notification.setId(notificationId);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        doNothing().when(notificationService).sendNotification(notification);

        notificationConsumer.processNotification(notificationId);

        verify(notificationRepository, times(1)).findById(notificationId);
        verify(notificationService, times(1)).sendNotification(notification);
    }

    @Test
    public void testProcessNotification_NotificationNotFound() {
        Long notificationId = 1L;

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        try {
            notificationConsumer.processNotification(notificationId);
        } catch (RuntimeException e) {
            assertEquals("Notification not found", e.getMessage());
        }

        verify(notificationRepository, times(1)).findById(notificationId);
        verify(notificationService, never()).sendNotification(any(Notification.class));
    }
}
