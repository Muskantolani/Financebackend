package org.example.apitestingproject.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    private static final Logger logger = LoggerFactory.getLogger(MailConfig.class);

    @Value("${gmail.username}")
    private String username;

    @Value("${gmail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        try {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

            logger.info("Configuring JavaMailSender with username: {}", username);
            logger.info("SMTP Host: smtp.gmail.com, Port: 587");

            // Basic configuration
            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(587);
            mailSender.setUsername(username);
            mailSender.setPassword(password);

            // JavaMail properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");

            // Additional properties for better Gmail compatibility
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            // Timeout settings
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.writetimeout", "10000");

            // Debug mode
            props.put("mail.debug", "true");
            props.put("mail.smtp.debug", "true");

            mailSender.setJavaMailProperties(props);

            logger.info("JavaMailSender configured successfully");
            return mailSender;

        } catch (Exception e) {
            logger.error("Failed to configure JavaMailSender: {}", e.getMessage(), e);
            throw new RuntimeException("Mail configuration failed", e);
        }
    }
}