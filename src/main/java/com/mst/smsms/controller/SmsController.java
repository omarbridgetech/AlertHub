package com.mst.smsms.controller;


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
    @PostMapping("/sendSms")

    @Operation(summary = "Send sms notification", description = "Sends an sms notification to the specified recipient")
    public String sendSmsMessage(@RequestBody SmsModel smsModel) {


        log.info("sendSms started sendRequst: "+smsModel.toString());
        return smsService.sendSms(smsModel.getDestinationSmsNumber(),smsModel.getSmsMessage());

    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the email service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Email service is running");
    }
}
