package org.example.apitestingproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class EmailDeliveryTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void testEmailDelivery() {
        System.out.println("🚀 Starting Email Delivery Test...");
        System.out.println("=========================================");

        try {
            // Create a real email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("aravinths132003@gmail.com"); // Send to yourself for testing
            message.setSubject("Test Email Delivery - Spring Boot Test");
            message.setText(
                    "This is a test email from your Spring Boot application.\n\n" +
                            "If you receive this email, your SMTP configuration is working correctly!\n\n" +
                            "Timestamp: " + java.time.LocalDateTime.now()
            );

            System.out.println("📧 Attempting to send email to: f82480@gmail.com");
            System.out.println("🔧 SMTP Configuration:");
            System.out.println("   - Host: smtp.gmail.com");
            System.out.println("   - Port: 587");
            System.out.println("   - Using TLS: Yes");

            // This will attempt actual delivery
            mailSender.send(message);

            System.out.println("✅ EMAIL SENT SUCCESSFULLY!");
            System.out.println("📨 Check your inbox at f82480@gmail.com for the test email.");
            System.out.println("💡 The email should arrive within 1-2 minutes.");

        } catch (MailAuthenticationException e) {
            System.out.println("❌ AUTHENTICATION FAILED!");
            System.out.println("=========================================");
            System.out.println("Error: " + e.getMessage());
            System.out.println("");
            System.out.println("🔧 SOLUTION:");
            System.out.println("1. Go to: https://myaccount.google.com/apppasswords");
            System.out.println("2. Generate a 16-character app password for 'Mail'");
            System.out.println("3. Update application.properties with the app password");
            System.out.println("4. Make sure you're NOT using your regular Gmail password");
            System.out.println("");
            System.out.println("💡 Your current password length: " +
                    (System.getProperty("spring.mail.password") != null ?
                            System.getProperty("spring.mail.password").length() : "unknown") + " characters");

        } catch (MailSendException e) {
            System.out.println("❌ SMTP DELIVERY FAILED!");
            System.out.println("=========================================");
            System.out.println("Error: " + e.getMessage());
            System.out.println("");
            System.out.println("🔧 Possible issues:");
            System.out.println("1. Network connection blocked");
            System.out.println("2. Gmail rate limiting");
            System.out.println("3. Invalid recipient email");

        } catch (Exception e) {
            System.out.println("❌ UNEXPECTED ERROR!");
            System.out.println("=========================================");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=========================================");
        System.out.println("🏁 Test completed.");
    }

    @Test
    void testSmtpConnectionOnly() {
        System.out.println("🔧 Testing SMTP Connection (without sending)...");

        try {
            // Just test connection by creating a message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("test@example.com");
            message.setSubject("Connection Test");
            message.setText("Test");

            mailSender.send(message); // This will fail at connection if auth is wrong

            System.out.println("✅ Connection test passed!");

        } catch (MailAuthenticationException e) {
            System.out.println("❌ Connection failed: Authentication error");
            System.out.println("Details: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getClass().getSimpleName());
            System.out.println("Details: " + e.getMessage());
        }
    }
}