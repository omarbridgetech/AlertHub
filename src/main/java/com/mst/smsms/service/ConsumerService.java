package com.mst.smsms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @Autowired
    private SmsService smsService;

    @KafkaListener(topics = "sms", groupId = "sms-consumer-group")
    public void consumeMessage(String message) {

        String[] part = message.split(",", 2);

        smsService.sendSms(part[0], part[1]);

    }
}

