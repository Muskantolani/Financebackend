package org.example.apitestingproject.service;


import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PasswordUpdateServices {
    private static final Logger logger = LoggerFactory.getLogger(PasswordUpdateServices.class);

    private final UserRepository userRepository;
    private final EmailServices emailServices;
    private final PasswordEncoder passwordEncoder;

    public PasswordUpdateServices (UserRepository userRepository,
                                EmailServices emailServices,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailServices = emailServices;
        this.passwordEncoder = passwordEncoder;
    }

    // Step 1: Send OTP
    public Map<String, Object> requestPasswordReset(String email) {
        return emailServices.sendOtp(email);
    }

    // Step 2: Verify OTP and update password
    public Map<String, Object> resetPassword(String email, String otp, String newPassword) {
        Map<String, Object> response = new HashMap<>();

        // Step 2.1 Verify OTP
        Map<String, Object> otpResult = emailServices.verifyOtp(email, otp);
        if (!(Boolean) otpResult.get("success")) {
            return otpResult; // return error (invalid/expired OTP)
        }

        // Step 2.2 Find user
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return response;
        }

        User user = userOptional.get();

        // Step 2.3 Hash and update password
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setPasswordUpdateTimestamp(LocalDateTime.now()); // update timestamp
        userRepository.save(user);

        logger.info("Password updated successfully for user: {}", email);

        // Step 2.4 Send confirmation email
        String subject = "Password Reset Confirmation";
        String message = "Hello " + user.getName() + ",\n\n"
                + "Your password has been updated successfully on "
                + LocalDateTime.now() + ".\n\n"
                + "If this was not you, please contact support immediately.\n\n"
                + "Best Regards,\nYour App Team";

        emailServices.sendSimpleMessage(email, subject, message);

        response.put("success", true);
        response.put("message", "Password updated successfully. Confirmation email sent.");
        return response;
    }
}

