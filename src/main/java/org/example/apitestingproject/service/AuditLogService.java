package org.example.apitestingproject.service;


import org.example.apitestingproject.entities.AuditLog;
import org.example.apitestingproject.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditLog saveLog(AuditLog log) {
        if (log.getActionTimestamp() == null) {
            log.setActionTimestamp(LocalDateTime.now());
        }
        return auditLogRepository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    public AuditLog getLogById(int id) {
        return auditLogRepository.findById(id).orElse(null);
    }

    public void deleteLog(int id) {
        auditLogRepository.deleteById(id);
    }
}