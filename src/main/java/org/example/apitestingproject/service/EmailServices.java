package org.example.apitestingproject.service;


import org.example.apitestingproject.entities.OtpVerification;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.OtpVerificationRepository;
import org.example.apitestingproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class EmailServices {
    private static final Logger logger = LoggerFactory.getLogger(EmailServices.class);

    private final JavaMailSender mailSender;
    private final OtpVerificationRepository otpVerificationRepository;
    private final UserRepository userRepository;

    public EmailServices(JavaMailSender mailSender,
                         OtpVerificationRepository otpVerificationRepository,
                         UserRepository userRepository) {
        this.mailSender = mailSender;
        this.otpVerificationRepository = otpVerificationRepository;
        this.userRepository = userRepository;
        logger.info("EmailServices initialized with database storage");
    }

    public Map<String, Object> sendOtp(String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate email format
            if (email == null || !email.contains("@")) {
                response.put("success", false);
                response.put("message", "Invalid email address");
                return response;
            }

            // Find user by email (you might need to adjust this based on your User entity)
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found with email: " + email);
                return response;
            }

            User user = userOptional.get();

            // Generate 6-digit OTP
            String otp = String.valueOf(new Random().nextInt(900000) + 100000);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiresAt = now.plusMinutes(10); // OTP expires in 10 minutes

            // Save OTP to database
            OtpVerification otpVerification = new OtpVerification();
            otpVerification.setUser(user);
            otpVerification.setPhoneNo("EMAIL_OTP"); // Or use actual phone if available
            otpVerification.setOtpCode(otp);
            otpVerification.setOtpTimestamp(now);
            otpVerification.setExpiresAt(expiresAt);
            otpVerification.setStatus(OtpVerification.Status.Pending);

            otpVerificationRepository.save(otpVerification);

            // Create and send email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP Verification Code");
            message.setText(
                    "Your OTP verification code is: " + otp +
                            "\n\nThis OTP will expire in 10 minutes." +
                            "\n\nIf you didn't request this code, please ignore this email."
            );
            message.setFrom("f82480@gmail.com"); // Use your actual email

            mailSender.send(message);

            logger.info("OTP sent successfully to: {}", email);
            logger.info("OTP saved to database with ID: {}", otpVerification.getId());

            response.put("success", true);
            response.put("message", "OTP sent successfully");
            response.put("otpId", otpVerification.getId()); // Return OTP ID for reference

        } catch (Exception e) {
            logger.error("Failed to send OTP to {}: {}", email, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Failed to send OTP: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> verifyOtp(String email, String otp) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Find user by email
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }

            User user = userOptional.get();
            LocalDateTime now = LocalDateTime.now();

            // Find the latest OTP for this user
            Optional<OtpVerification> otpOptional = otpVerificationRepository
                    .findTopByUserAndStatusOrderByOtpTimestampDesc(user, OtpVerification.Status.Pending);

            if (otpOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "OTP not found or already used");
                return response;
            }

            OtpVerification otpVerification = otpOptional.get();

            // Check if OTP is expired
            if (now.isAfter(otpVerification.getExpiresAt())) {
                otpVerification.setStatus(OtpVerification.Status.Expired);
                otpVerificationRepository.save(otpVerification);

                response.put("success", false);
                response.put("message", "OTP has expired");
                return response;
            }

            // Verify OTP code
            if (otpVerification.getOtpCode().equals(otp)) {
                otpVerification.setStatus(OtpVerification.Status.Verified);
                otpVerificationRepository.save(otpVerification);

                logger.info("OTP verified successfully for user: {}", email);
                response.put("success", true);
                response.put("message", "OTP verified successfully");
            } else {
                response.put("success", false);
                response.put("message", "Invalid OTP");
            }

        } catch (Exception e) {
            logger.error("Error verifying OTP for {}: {}", email, e.getMessage());
            response.put("success", false);
            response.put("message", "Error verifying OTP");
        }

        return response;
    }

    // Cleanup expired OTPs (can be scheduled)
    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        int expiredCount = otpVerificationRepository.markExpiredOtps(now, OtpVerification.Status.Expired);
        logger.info("Cleaned up {} expired OTPs", expiredCount);
    }
}