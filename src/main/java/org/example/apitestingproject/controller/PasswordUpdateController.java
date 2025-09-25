package org.example.apitestingproject.controller;


import org.example.apitestingproject.service.PasswordUpdateServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/password")
public class PasswordUpdateController {

    private final PasswordUpdateServices passwordResetService;

    public PasswordUpdateController(PasswordUpdateServices passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    // Step 1: Request OTP
    @PostMapping("/reset/request")
    public ResponseEntity<Map<String, Object>> requestPasswordReset(@RequestParam String email) {
        Map<String, Object> response = passwordResetService.requestPasswordReset(email);
        return ResponseEntity.ok(response);
    }

    // Step 2: Confirm Reset with OTP + New Password
    @PostMapping("/reset/confirm")
    public ResponseEntity<Map<String, Object>> confirmPasswordReset(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        Map<String, Object> response = passwordResetService.resetPassword(email, otp, newPassword);
        return ResponseEntity.ok(response);
    }
}
