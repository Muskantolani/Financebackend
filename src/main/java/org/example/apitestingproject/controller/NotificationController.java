package org.example.apitestingproject.controller;

import org.example.apitestingproject.entities.Notification;
import org.example.apitestingproject.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam int userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable int id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}

