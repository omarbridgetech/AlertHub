package com.mst.emailms.controller;


import com.mst.emailms.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications/email")
@Tag(name = "Email Notification", description = "Endpoints for sending email notifications")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/send")
    @Operation(summary = "Send email notification", description = "Sends an email notification to the specified recipient")
    public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        //logger.info("Received request to send email to: {}", to);

        try {
            emailService.sendEmailMessage(to, subject, text);
            //logger.info("Email sent successfully to: {}", to);
            return ResponseEntity.ok("Email sent successfully to: " + to);
        } catch (IllegalArgumentException e) {
            //logger.error("Invalid email parameters: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid parameters: " + e.getMessage());
        } catch (Exception e) {
            //logger.error("Error sending email to {}: {}", to, e.getMessage());
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the email service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Email service is running");
    }




}
