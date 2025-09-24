package org.example.apitestingproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.example.apitestingproject.DTO.AudiLogDTO;
import org.example.apitestingproject.entities.AuditLog;
import org.example.apitestingproject.repository.AdminRepository;
import org.example.apitestingproject.repository.UserRepository;
import org.example.apitestingproject.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    @PostMapping
    public ResponseEntity<?> createLog(@RequestBody AudiLogDTO dto,
                                       HttpServletRequest request) {
        try {
            AuditLog log = new AuditLog();



            // DTO to entity
            log.setActionType(AuditLog.ActionType.valueOf(dto.getActionType()));
            if (dto.getUserId() != null)
                log.setUser(userRepository.findById(dto.getUserId()).orElse(null));
            if (dto.getAdminId() != null)
                log.setAdmin(adminRepository.findById(dto.getAdminId()).orElse(null));


            ObjectMapper mapper = new ObjectMapper();
            String detailsJson = mapper.writeValueAsString(dto.getActionDetails());
            log.setActionDetails(detailsJson);




            log.setActionTimestamp(LocalDateTime.now());
            log.setIpAddress(extractClientIp(request));

            AuditLog saved = auditLogService.saveLog(log);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving log: " + e.getMessage());
        }
    }




    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); //
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        } else {

            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getLogById(@PathVariable int id) {
        AuditLog log = auditLogService.getLogById(id);
        return log != null ? ResponseEntity.ok(log) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable int id) {
        auditLogService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }







    // Finding how payments are being made
    // Finding History of each User
    //




}
