package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.AuditLog;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<AuditLog,Integer> {
}
