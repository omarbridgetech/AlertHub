package com.mst.emailms.service;


import com.mst.emailms.client.LoggerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoggerClient loggerClient;

    @KafkaListener(topics = "email", groupId = "email-consumer-group")
    public void consumeMessage(String message) {
        log.info("Received message from Kafka: {}", message);
        loggerClient.info("Received message from Kafka email topic");


        try {

            String[] part = message.split(",", 2);

            if (part.length < 2) {
                loggerClient.error("Invalid message format: " + message);
                return;
            }

            emailService.sendEmailMessage(part[0], "email from kafka", part[1]);
            loggerClient.info("Processed Kafka message successfully for: " + part[0]);

        }
        catch (Exception e) {
            log.error("Error processing Kafka message: {}", e.getMessage());
            loggerClient.error("Error processing Kafka message: " + e.getMessage());

        }

    }


}
