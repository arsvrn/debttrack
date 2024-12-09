package com.debttrack.platfrom.service;

import com.debttrack.platfrom.enums.NotificationStatus;
import com.debttrack.platfrom.model.Notification;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendNotification() {
        User user = new User();
        user.setEmail("test@example.com");

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage("Test notification message");
        notification.setStatus(NotificationStatus.PENDING);

        notificationService.sendNotification(notification);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertEquals("test@example.com", message.getTo()[0]);
        assertEquals("Напоминание о долге", message.getSubject());
        assertEquals("Test notification message", message.getText());

        verify(notificationRepository, times(1)).save(notification);
        assertEquals(NotificationStatus.SENT, notification.getStatus());
        assertNotNull(notification.getSentAt());
    }

    @Test
    public void testSendPendingNotifications() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        User user2 = new User();
        user2.setEmail("user2@example.com");

        Notification notification1 = new Notification();
        notification1.setUser(user1);
        notification1.setMessage("Notification 1");
        notification1.setStatus(NotificationStatus.PENDING);

        Notification notification2 = new Notification();
        notification2.setUser(user2);
        notification2.setMessage("Notification 2");
        notification2.setStatus(NotificationStatus.PENDING);

        when(notificationRepository.findByStatus(NotificationStatus.PENDING)).thenReturn(Arrays.asList(notification1, notification2));

        notificationService.sendPendingNotifications();

        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
        verify(notificationRepository, times(2)).save(any(Notification.class));

        assertEquals(NotificationStatus.SENT, notification1.getStatus());
        assertEquals(NotificationStatus.SENT, notification2.getStatus());
        assertNotNull(notification1.getSentAt());
        assertNotNull(notification2.getSentAt());
    }
}
