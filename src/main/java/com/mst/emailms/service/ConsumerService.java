package com.mst.emailms.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service

public class ConsumerService {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "emailTopic", groupId = "omar's")
    public void consumeMessage(String message) {

        String[] part = message.split(",", 2);

        emailService.sendEmailMessage(part[0], "email from kafka", part[1]);

    }


}
