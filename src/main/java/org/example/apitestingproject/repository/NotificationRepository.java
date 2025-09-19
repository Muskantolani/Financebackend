package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification,Integer> {
}
