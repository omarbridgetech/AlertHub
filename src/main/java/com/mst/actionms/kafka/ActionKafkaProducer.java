package com.mst.actionms.kafka;

import com.mst.actionms.dto.ActionExecutionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ActionKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(ActionKafkaProducer.class);

    private final KafkaTemplate<String, ActionExecutionMessage> kafkaTemplate;
    private final String topicName;

    public ActionKafkaProducer(
            KafkaTemplate<String, ActionExecutionMessage> kafkaTemplate,
            @Value("${alert.actions.topic}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void send(ActionExecutionMessage message) {
        String key = message.getActionId() != null
                ? message.getActionId().toString()
                : null;

        logger.info("Sending ActionExecutionMessage to Kafka topic {} with key {}: {}",
                topicName, key, message);

        kafkaTemplate.send(topicName, key, message);
    }
}
