package com.mst.smsms.controller;


import com.mst.smsms.client.LoggerClient;
import com.mst.smsms.model.SmsModel;
import com.mst.smsms.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/notifications/sms")
@Slf4j
@Tag(name = "Sms Notification", description = "Endpoints for sending sms notifications")
public class SmsController {

    @Autowired
    SmsService smsService;

    @Autowired
    private LoggerClient loggerClient;
    @PostMapping("/sendSms")

    @Operation(summary = "Send sms notification", description = "Sends an sms notification to the specified recipient")
    public String sendSmsMessage(@RequestBody SmsModel smsModel) {

        log.info("sendSms started, request: {}", smsModel.toString());
        loggerClient.info("Received REST request to send SMS to: " + smsModel.getDestinationSmsNumber());

        try {
            String status = smsService.sendSms(smsModel.getDestinationSmsNumber(), smsModel.getSmsMessage());
            return status;

        } catch (Exception e) {
            log.error("Error sending SMS: {}", e.getMessage());
            loggerClient.error("Error sending SMS via REST: " + e.getMessage());
            return ("Error sending SMS: " + e.getMessage());
        }

    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the email service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Email service is running");
    }
}
