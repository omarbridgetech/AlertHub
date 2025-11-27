package com.mst.processorms.kafka;



import com.mst.processorms.dto.ActionExecutionMessage;
import com.mst.processorms.dto.ActionType;
import com.mst.processorms.dto.NotificationMessage;
import org.springframework.kafka.core.KafkaTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaProducer {

    private final KafkaTemplate<String, NotificationMessage> notificationKafkaTemplate;

    @Value("${alert.notifications.email-topic}")
    private String emailTopic;

    @Value("${alert.notifications.sms-topic}")
    private String smsTopic;

    public void sendNotification(ActionExecutionMessage action) {
        NotificationMessage msg = new NotificationMessage();
        msg.setTo(action.getTo());
        msg.setMessage(action.getMessage());

        String topic = (action.getActionType() == ActionType.EMAIL) ? emailTopic : smsTopic;

        log.info("Sending notification to topic {}: {}", topic, msg);
        notificationKafkaTemplate.send(topic, msg.getTo(), msg);
    }
}
