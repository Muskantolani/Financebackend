package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.Notification;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.NotificationRepository;
import org.example.apitestingproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void createNotification(int userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setNotificationDate(LocalDateTime.now());
        notification.setStatus(Notification.Status.Unread);

        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(int userId) {
        return notificationRepository.findByUserIdOrderByNotificationDateDesc(userId);
    }

    public void markAsRead(int notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setStatus(Notification.Status.Read);
        notificationRepository.save(notification);
    }
}
