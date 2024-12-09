package com.debttrack.platfrom.controller;

import com.debttrack.platfrom.model.Group;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestParam String name, @RequestParam Long adminId) {
        try {
            Group group = groupService.createGroup(name, adminId);
            return new ResponseEntity<>(group, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input parameters", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> addMember(@PathVariable Long groupId, @RequestParam Long userId) {
        try {
            groupService.addUserToGroup(groupId, userId);
            return new ResponseEntity<>("User added to group", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input parameters", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add user to group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{groupId}/members")
    public ResponseEntity<?> removeMember(@PathVariable Long groupId, @RequestParam Long userId) {
        try {
            groupService.removeUserFromGroup(groupId, userId);
            return new ResponseEntity<>("User removed from group", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input parameters", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to remove user from group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<?> getMembers(@PathVariable Long groupId) {
        try {
            List<User> members = groupService.getGroupMembers(groupId);
            return new ResponseEntity<>(members, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Group not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve group members", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
