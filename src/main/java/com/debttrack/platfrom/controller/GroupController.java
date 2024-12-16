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
        Group group = groupService.createGroup(name, adminId);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> addMember(@PathVariable Long groupId, @RequestParam Long userId) {
        groupService.addUserToGroup(groupId, userId);
        return new ResponseEntity<>("User added to group", HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/members")
    public ResponseEntity<?> removeMember(@PathVariable Long groupId, @RequestParam Long userId) {
        groupService.removeUserFromGroup(groupId, userId);
        return new ResponseEntity<>("User removed from group", HttpStatus.OK);
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<?> getMembers(@PathVariable Long groupId) {
        List<User> members = groupService.getGroupMembers(groupId);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>("Invalid input parameters", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
