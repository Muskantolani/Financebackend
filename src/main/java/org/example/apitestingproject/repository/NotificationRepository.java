package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification,Integer> {
    List<Notification> findByUserIdOrderByNotificationDateDesc(int userId);
}
