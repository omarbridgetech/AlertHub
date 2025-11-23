package com.mst.emailms.service;


import com.mst.emailms.client.LoggerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private LoggerClient loggerClient;


    public void sendEmailMessage(String to, String subject, String text) {
        try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

            // Log success to Logger microservice
            log.info("Email sent successfully to: {}", to);
            loggerClient.info("Email sent successfully to: " + to);
        } catch (Exception e) {
            // Log error to Logger microservice
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            loggerClient.error("Failed to send email to " + to + ": " + e.getMessage());
            throw e;

        }

    }
}
