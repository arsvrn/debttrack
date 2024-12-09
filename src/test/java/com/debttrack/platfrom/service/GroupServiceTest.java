package com.debttrack.platfrom.service;

import com.debttrack.platfrom.model.Group;
import com.debttrack.platfrom.model.GroupUser;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.repository.GroupRepository;
import com.debttrack.platfrom.repository.GroupUserRepository;
import com.debttrack.platfrom.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupUserRepository groupUserRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateGroup() {
        String name = "Test Group";
        Long adminId = 1L;
        User admin = new User();
        admin.setId(adminId);

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));

        Group group = new Group();
        group.setName(name);
        group.setAdmin(admin);

        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group result = groupService.createGroup(name, adminId);

        assertEquals(group, result);
    }

    @Test
    public void testAddUserToGroup() {
        Long groupId = 1L;
        Long userId = 2L;
        Group group = new Group();
        group.setId(groupId);
        User user = new User();
        user.setId(userId);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(groupUserRepository.findByGroup(group)).thenReturn(List.of());

        groupService.addUserToGroup(groupId, userId);

        verify(groupUserRepository, times(1)).save(any(GroupUser.class));
    }

    @Test
    public void testAddUserToGroup_UserAlreadyInGroup() {
        Long groupId = 1L;
        Long userId = 2L;
        Group group = new Group();
        group.setId(groupId);
        User user = new User();
        user.setId(userId);
        GroupUser groupUser = new GroupUser();
        groupUser.setGroup(group);
        groupUser.setUser(user);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(groupUserRepository.findByGroup(group)).thenReturn(Arrays.asList(groupUser));

        assertThrows(RuntimeException.class, () -> {
            groupService.addUserToGroup(groupId, userId);
        });
    }

    @Test
    public void testRemoveUserFromGroup() {
        Long groupId = 1L;
        Long userId = 2L;
        Group group = new Group();
        group.setId(groupId);
        User user = new User();
        user.setId(userId);
        GroupUser groupUser = new GroupUser();
        groupUser.setGroup(group);
        groupUser.setUser(user);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(groupUserRepository.findByGroup(group)).thenReturn(Arrays.asList(groupUser));

        groupService.removeUserFromGroup(groupId, userId);

        verify(groupUserRepository, times(1)).delete(groupUser);
    }

    @Test
    public void testGetGroupMembers() {
        Long groupId = 1L;
        Group group = new Group();
        group.setId(groupId);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        GroupUser groupUser1 = new GroupUser();
        groupUser1.setGroup(group);
        groupUser1.setUser(user1);
        GroupUser groupUser2 = new GroupUser();
        groupUser2.setGroup(group);
        groupUser2.setUser(user2);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupUserRepository.findByGroup(group)).thenReturn(Arrays.asList(groupUser1, groupUser2));

        List<User> result = groupService.getGroupMembers(groupId);

        assertEquals(2, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));
    }
}
