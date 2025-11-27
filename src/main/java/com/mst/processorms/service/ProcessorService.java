package com.mst.processorms.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mst.processorms.client.LoaderClient;
import com.mst.processorms.client.MetricsClient;
import com.mst.processorms.dto.ActionExecutionMessage;
import com.mst.processorms.dto.MetricResponseDTO;
import com.mst.processorms.kafka.NotificationKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessorService {

    private final MetricsClient metricsClient;
    private final LoaderClient loaderClient;
    private final NotificationKafkaProducer notificationKafkaProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processAction(ActionExecutionMessage msg) {
        try {
            log.info("Processing action: {}", msg);

            // 1. Parse condition JSON to List<List<UUID>>
            List<List<UUID>> conditionGroups = parseCondition(msg.getCondition());

            // 2. Evaluate condition via Metrics + Loader (no DB here)
            boolean conditionSatisfied = evaluateCondition(msg.getUserId(), conditionGroups);

            if (!conditionSatisfied) {
                log.info("Condition NOT satisfied for action {}", msg.getActionId());
                return;
            }

            // 3. If condition true → send notification to email/sms topic
            notificationKafkaProducer.sendNotification(msg);
            log.info("Condition satisfied for action {}, notification sent", msg.getActionId());

        } catch (Exception e) {
            log.error("Error processing action {}: {}", msg.getActionId(), e.getMessage(), e);
        }
    }

    private List<List<UUID>> parseCondition(String conditionJson) throws Exception {
        if (conditionJson == null || conditionJson.isBlank()) {
            throw new IllegalArgumentException("Condition JSON is null or empty");
        }

        return objectMapper.readValue(
                conditionJson,
                new TypeReference<List<List<UUID>>>() {}
        );
    }

    private boolean evaluateCondition(String ownerId, List<List<UUID>> groups) {
        // OR between groups
        for (List<UUID> group : groups) {
            boolean groupOk = true;

            // AND inside each group
            for (UUID metricId : group) {
                MetricResponseDTO metric = metricsClient.getMetricDetails(metricId.toString());

                String labelValue = metric.getLabel().toString(); // "bug", "help_wanted", ...

                boolean metricSatisfied = loaderClient.checkLabelThreshold(
                        ownerId,
                        labelValue,
                        metric.getTimeFrameHours(),
                        metric.getThreshold()
                );

                if (!metricSatisfied) {
                    groupOk = false;
                    break;
                }
            }

            if (groupOk) {
                return true; // at least one group is fully satisfied
            }
        }

        return false;
    }
}
