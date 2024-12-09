package com.debttrack.platfrom.controller;

import com.debttrack.platfrom.model.Group;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateGroup_Success() {
        String name = "Test Group";
        Long adminId = 1L;
        Group group = new Group();
        group.setName(name);
        when(groupService.createGroup(name, adminId)).thenReturn(group);

        ResponseEntity<?> response = groupController.createGroup(name, adminId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(group, response.getBody());
        verify(groupService, times(1)).createGroup(name, adminId);
    }

    @Test
    public void testCreateGroup_InvalidInput() {
        String name = "Test Group";
        Long adminId = 1L;
        when(groupService.createGroup(name, adminId)).thenThrow(new IllegalArgumentException("Invalid input parameters"));

        ResponseEntity<?> response = groupController.createGroup(name, adminId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input parameters", response.getBody());
        verify(groupService, times(1)).createGroup(name, adminId);
    }

    @Test
    public void testCreateGroup_InternalServerError() {
        String name = "Test Group";
        Long adminId = 1L;
        when(groupService.createGroup(name, adminId)).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<?> response = groupController.createGroup(name, adminId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to create group", response.getBody());
        verify(groupService, times(1)).createGroup(name, adminId);
    }

    @Test
    public void testAddMember_Success() {
        Long groupId = 1L;
        Long userId = 2L;
        doNothing().when(groupService).addUserToGroup(groupId, userId);

        ResponseEntity<?> response = groupController.addMember(groupId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User added to group", response.getBody());
        verify(groupService, times(1)).addUserToGroup(groupId, userId);
    }

    @Test
    public void testAddMember_InvalidInput() {
        Long groupId = 1L;
        Long userId = 2L;
        doThrow(new IllegalArgumentException("Invalid input parameters")).when(groupService).addUserToGroup(groupId, userId);

        ResponseEntity<?> response = groupController.addMember(groupId, userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input parameters", response.getBody());
        verify(groupService, times(1)).addUserToGroup(groupId, userId);
    }

    @Test
    public void testAddMember_InternalServerError() {
        Long groupId = 1L;
        Long userId = 2L;
        doThrow(new RuntimeException("Internal server error")).when(groupService).addUserToGroup(groupId, userId);

        ResponseEntity<?> response = groupController.addMember(groupId, userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to add user to group", response.getBody());
        verify(groupService, times(1)).addUserToGroup(groupId, userId);
    }

    @Test
    public void testRemoveMember_Success() {
        Long groupId = 1L;
        Long userId = 2L;
        doNothing().when(groupService).removeUserFromGroup(groupId, userId);

        ResponseEntity<?> response = groupController.removeMember(groupId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User removed from group", response.getBody());
        verify(groupService, times(1)).removeUserFromGroup(groupId, userId);
    }

    @Test
    public void testRemoveMember_InvalidInput() {
        Long groupId = 1L;
        Long userId = 2L;
        doThrow(new IllegalArgumentException("Invalid input parameters")).when(groupService).removeUserFromGroup(groupId, userId);

        ResponseEntity<?> response = groupController.removeMember(groupId, userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input parameters", response.getBody());
        verify(groupService, times(1)).removeUserFromGroup(groupId, userId);
    }

    @Test
    public void testRemoveMember_InternalServerError() {
        Long groupId = 1L;
        Long userId = 2L;
        doThrow(new RuntimeException("Internal server error")).when(groupService).removeUserFromGroup(groupId, userId);

        ResponseEntity<?> response = groupController.removeMember(groupId, userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to remove user from group", response.getBody());
        verify(groupService, times(1)).removeUserFromGroup(groupId, userId);
    }

    @Test
    public void testGetMembers_Success() {
        Long groupId = 1L;
        List<User> members = Arrays.asList(new User(), new User());
        when(groupService.getGroupMembers(groupId)).thenReturn(members);

        ResponseEntity<?> response = groupController.getMembers(groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(members, response.getBody());
        verify(groupService, times(1)).getGroupMembers(groupId);
    }

    @Test
    public void testGetMembers_GroupNotFound() {
        Long groupId = 1L;
        when(groupService.getGroupMembers(groupId)).thenThrow(new IllegalArgumentException("Group not found"));

        ResponseEntity<?> response = groupController.getMembers(groupId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Group not found", response.getBody());
        verify(groupService, times(1)).getGroupMembers(groupId);
    }

    @Test
    public void testGetMembers_InternalServerError() {
        Long groupId = 1L;
        when(groupService.getGroupMembers(groupId)).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<?> response = groupController.getMembers(groupId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to retrieve group members", response.getBody());
        verify(groupService, times(1)).getGroupMembers(groupId);
    }
}
