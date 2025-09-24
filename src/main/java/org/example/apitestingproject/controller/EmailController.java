package org.example.apitestingproject.controller;

import org.example.apitestingproject.service.EmailServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class EmailController {

    private final EmailServices emailServices;

    public EmailController(EmailServices emailServices) {
        this.emailServices = emailServices;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestParam String email) {
        Map<String, Object> result = emailServices.sendOtp(email);
        boolean success = (Boolean) result.get("success");

        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        Map<String, Object> result = emailServices.verifyOtp(email, otp);
        boolean success = (Boolean) result.get("success");

        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}