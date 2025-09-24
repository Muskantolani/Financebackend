package org.example.apitestingproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class EmailConnectionTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void testSmtpConnection() {
        try {
            System.out.println("🔧 Testing SMTP connection...");

            // Create a simple test message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("test@example.com"); // This won't actually send if connection fails
            message.setSubject("Test Connection");
            message.setText("This is a test message");

            // This will fail at the connection stage if authentication is wrong
            mailSender.send(message);

            System.out.println("✅ Mail configuration seems correct!");

        } catch (org.springframework.mail.MailAuthenticationException e) {
            System.out.println("❌ AUTHENTICATION FAILED: " + e.getMessage());
            System.out.println("💡 Please check:");
            System.out.println("   - Are you using an APP PASSWORD (16 characters) not your regular password?");
            System.out.println("   - Is 2-factor authentication enabled?");
            System.out.println("   - Is the email address correct?");
        } catch (Exception e) {
            System.out.println("❌ Other error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}