package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.model.Group;
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
public class GroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    private User admin;
    private User anotherAdmin;

    @BeforeEach
    public void setUp() {

        admin = new User();
        admin.setEmail("admin@example.com");
        admin.setName("Admin");
        admin.setPasswordHash("hashed_password");
        admin.setRole(com.debttrack.platfrom.enums.Role.USER);
        admin = userRepository.save(admin);

         anotherAdmin = new User();
        anotherAdmin.setEmail("anotheradmin@example.com");
        anotherAdmin.setName("Another Admin");
        anotherAdmin.setPasswordHash("hashed_password");
        anotherAdmin.setRole(com.debttrack.platfrom.enums.Role.USER);
        anotherAdmin = userRepository.save(anotherAdmin);

        Group group1 = new Group();
        group1.setName("Group 1");
        group1.setAdmin(admin);
        entityManager.persist(group1);

        Group group2 = new Group();
        group2.setName("Group 2");
        group2.setAdmin(admin);
        entityManager.persist(group2);

        Group group3 = new Group();
        group3.setName("Group 3");
        group3.setAdmin(anotherAdmin);
        entityManager.persist(group3);
    }

    @Test
    public void testFindByAdmin() {
        List<Group> groups = groupRepository.findByAdmin(admin);
        assertEquals(2, groups.size());
        for (Group group : groups) {
            assertEquals(admin, group.getAdmin());
        }
    }

    @Test
    public void testFindByAdmin_NoGroups() {
        User newAdmin = new User();
        newAdmin.setEmail("newadmin@example.com");
        newAdmin.setName("New Admin");
        newAdmin.setPasswordHash("hashed_password");
        newAdmin.setRole(com.debttrack.platfrom.enums.Role.USER);
        newAdmin = userRepository.save(newAdmin);

        List<Group> groups = groupRepository.findByAdmin(newAdmin);
        assertEquals(0, groups.size());
    }
}
