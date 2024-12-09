package com.debttrack.platfrom.controller;

import com.debttrack.platfrom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{userId}/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateProfile(@PathVariable Long userId, @RequestParam String name, @RequestParam String email) {
        try {
            userService.updateProfile(userId, name, email);
            return new ResponseEntity<>("Profile updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input parameters", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update profile", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}/notifications")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateNotificationSettings(@PathVariable Long userId, @RequestParam boolean enableNotifications) {
        try {
            userService.updateNotificationSettings(userId, enableNotifications);
            return new ResponseEntity<>("Notification settings updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input parameters", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update notification settings", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
