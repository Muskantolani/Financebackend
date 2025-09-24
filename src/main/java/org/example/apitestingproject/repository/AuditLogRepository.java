package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.AuditLog;
import org.example.apitestingproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuditLogRepository extends JpaRepository<AuditLog,Integer> {
    Optional<AuditLog> findFirstByUserAndActionTypeAndActionDetailsContainingOrderByActionTimestampDesc(
            User user, AuditLog.ActionType actionType, String installmentIdString);

}
