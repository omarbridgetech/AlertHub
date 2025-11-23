package com.mst.smsms.service;

import com.mst.smsms.client.LoggerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private LoggerClient loggerClient;

    @KafkaListener(topics = "sms", groupId = "sms-consumer-group")
    public void consumeMessage(String message) {

        log.info("Received message from Kafka: {}", message);
        loggerClient.info("Received message from Kafka sms topic");

        try{
            String[] part = message.split(",", 2);

            if (part.length < 2) {
                loggerClient.error("Invalid message format: " + message);
                return;
            }
            smsService.sendSms(part[0], part[1]);
            loggerClient.info("Processed Kafka message successfully for: " + part[0]);
        }catch(Exception e){
            log.error("Error processing Kafka message: {}", e.getMessage());
            loggerClient.error("Error processing Kafka message: " + e.getMessage());
        }



    }
}

