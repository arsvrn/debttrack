package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.enums.NotificationStatus;
import com.debttrack.platfrom.model.Notification;
import com.debttrack.platfrom.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
public class NotificationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("user@example.com");
        user.setName("User");
        user.setPasswordHash("hashed_password");
        user.setRole(com.debttrack.platfrom.enums.Role.USER);
        user = userRepository.save(user);

         Notification notification1 = new Notification();
        notification1.setUser(user);
        notification1.setMessage("Notification 1");
        notification1.setStatus(NotificationStatus.PENDING);
        notification1.setSentAt(LocalDateTime.now());
        entityManager.persist(notification1);

        Notification notification2 = new Notification();
        notification2.setUser(user);
        notification2.setMessage("Notification 2");
        notification2.setStatus(NotificationStatus.PENDING);
        notification2.setSentAt(LocalDateTime.now());
        entityManager.persist(notification2);

        Notification notification3 = new Notification();
        notification3.setUser(user);
        notification3.setMessage("Notification 3");
        notification3.setStatus(NotificationStatus.SENT);
        notification3.setSentAt(LocalDateTime.now());
        entityManager.persist(notification3);
    }

    @Test
    public void testFindByStatus_Pending() {
        List<Notification> notifications = notificationRepository.findByStatus(NotificationStatus.PENDING);
        assertEquals(2, notifications.size());
        for (Notification notification : notifications) {
            assertEquals(NotificationStatus.PENDING, notification.getStatus());
        }
    }

    @Test
    public void testFindByStatus_Sent() {
        List<Notification> notifications = notificationRepository.findByStatus(NotificationStatus.SENT);
        assertEquals(1, notifications.size());
        for (Notification notification : notifications) {
            assertEquals(NotificationStatus.SENT, notification.getStatus());
        }
    }
}
