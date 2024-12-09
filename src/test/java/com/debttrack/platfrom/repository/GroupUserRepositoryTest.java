package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.model.Group;
import com.debttrack.platfrom.model.GroupUser;
import com.debttrack.platfrom.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
public class GroupUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    private Group group;
    private User user1;
    private User user2;
    private User admin;

    @BeforeEach
    public void setUp() {
        // Создание и сохранение администратора
        admin = new User();
        admin.setEmail("admin@example.com");
        admin.setName("Admin");
        admin.setPasswordHash("hashed_password");
        admin.setRole(com.debttrack.platfrom.enums.Role.ADMIN);
        admin = userRepository.save(admin);

        // Создание и сохранение группы
        group = new Group();
        group.setName("Test Group");
        group.setAdmin(admin); // Установка администратора для группы
        group = groupRepository.save(group);

        // Создание и сохранение пользователей
        user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setName("User 1");
        user1.setPasswordHash("hashed_password");
        user1.setRole(com.debttrack.platfrom.enums.Role.USER);
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setName("User 2");
        user2.setPasswordHash("hashed_password");
        user2.setRole(com.debttrack.platfrom.enums.Role.USER);
        user2 = userRepository.save(user2);

        // Создание и сохранение связей группы и пользователей
        GroupUser groupUser1 = new GroupUser();
        groupUser1.setGroup(group);
        groupUser1.setUser(user1);
        entityManager.persist(groupUser1);

        GroupUser groupUser2 = new GroupUser();
        groupUser2.setGroup(group);
        groupUser2.setUser(user2);
        entityManager.persist(groupUser2);
    }

    @Test
    public void testFindByGroup() {
        List<GroupUser> groupUsers = groupUserRepository.findByGroup(group);
        assertEquals(2, groupUsers.size());
        for (GroupUser groupUser : groupUsers) {
            assertEquals(group, groupUser.getGroup());
        }
    }

    @Test
    public void testFindByGroup_NoGroupUsers() {
        User newAdmin = new User();
        newAdmin.setEmail("newadmin@example.com");
        newAdmin.setName("New Admin");
        newAdmin.setPasswordHash("hashed_password");
        newAdmin.setRole(com.debttrack.platfrom.enums.Role.ADMIN);
        newAdmin = userRepository.save(newAdmin);

        Group newGroup = new Group();
        newGroup.setName("New Group");
        newGroup.setAdmin(newAdmin); // Установка администратора для новой группы
        newGroup = groupRepository.save(newGroup);

        List<GroupUser> groupUsers = groupUserRepository.findByGroup(newGroup);
        assertEquals(0, groupUsers.size());
    }
}
