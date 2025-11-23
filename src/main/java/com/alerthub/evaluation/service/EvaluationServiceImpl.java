package com.alerthub.evaluation.service;

import com.alerthub.evaluation.dto.*;
import com.alerthub.evaluation.exception.BadRequestException;
import com.alerthub.evaluation.exception.ResourceNotFoundException;
import com.alerthub.evaluation.repository.PlatformInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvaluationServiceImpl implements EvaluationService {

    private final PlatformInformationRepository platformInformationRepository;
    private final KafkaTemplate<String, EvaluationResultMessage> kafkaTemplate;

    @Value("${evaluation.manager.email:manager@example.com}")
    private String managerEmail;

    private static final String EMAIL_TOPIC = "email";

    @Override
    @Transactional(readOnly = true)
    public MostLabelDeveloperResponse findDeveloperWithMostLabel(String label, int sinceDays) {
        log.info("Finding developer with most occurrences of label: {} in the last {} days", label, sinceDays);
        
        validateSinceDays(sinceDays);
        
        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime fromDate = toDate.minusDays(sinceDays);
        
        List<Object[]> results = platformInformationRepository.findDeveloperWithMostTasksByLabel(
                label, fromDate, toDate);
        
        if (results.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No developer found with label '" + label + "' in the last " + sinceDays + " days");
        }
        
        Object[] topResult = results.get(0);
        String developerId = (String) topResult[0];
        Long count = (Long) topResult[1];
        
        MostLabelDeveloperResponse response = MostLabelDeveloperResponse.builder()
                .developerId(developerId)
                .label(label)
                .count(count)
                .sinceDays(sinceDays)
                .build();
        
        // Send notification to Kafka
        sendNotification(
                "Developer with Most " + label + " Tasks",
                String.format("Developer %s has the most tasks with label '%s': %d tasks in the last %d days.",
                        developerId, label, count, sinceDays)
        );
        
        log.info("Found developer {} with {} tasks for label {} in the last {} days", 
                developerId, count, label, sinceDays);
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public DeveloperLabelAggregationResponse aggregateLabelsForDeveloper(String developerId, int sinceDays) {
        log.info("Aggregating labels for developer: {} in the last {} days", developerId, sinceDays);
        
        validateSinceDays(sinceDays);
        
        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime fromDate = toDate.minusDays(sinceDays);
        
        List<Object[]> results = platformInformationRepository.aggregateLabelsByDeveloper(
                developerId, fromDate, toDate);
        
        List<LabelCountDto> labelCounts = results.stream()
                .map(result -> LabelCountDto.builder()
                        .label((String) result[0])
                        .count((Long) result[1])
                        .build())
                .collect(Collectors.toList());
        
        DeveloperLabelAggregationResponse response = DeveloperLabelAggregationResponse.builder()
                .developerId(developerId)
                .sinceDays(sinceDays)
                .labelCounts(labelCounts)
                .build();
        
        // Send notification to Kafka
        StringBuilder labelSummary = new StringBuilder();
        labelCounts.forEach(lc -> 
                labelSummary.append(String.format("\n- %s: %d tasks", lc.getLabel(), lc.getCount())));
        
        String body = labelCounts.isEmpty() 
                ? String.format("Developer %s has no tasks in the last %d days.", developerId, sinceDays)
                : String.format("Label aggregation for developer %s in the last %d days:%s", 
                        developerId, sinceDays, labelSummary.toString());
        
        sendNotification("Developer Label Aggregation", body);
        
        log.info("Aggregated {} labels for developer {} in the last {} days", 
                labelCounts.size(), developerId, sinceDays);
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public DeveloperTaskAmountResponse getTaskAmountForDeveloper(String developerId, int sinceDays) {
        log.info("Getting task amount for developer: {} in the last {} days", developerId, sinceDays);
        
        validateSinceDays(sinceDays);
        
        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime fromDate = toDate.minusDays(sinceDays);
        
        long taskCount = platformInformationRepository.countTasksByDeveloper(
                developerId, fromDate, toDate);
        
        DeveloperTaskAmountResponse response = DeveloperTaskAmountResponse.builder()
                .developerId(developerId)
                .sinceDays(sinceDays)
                .taskCount(taskCount)
                .build();
        
        // Send notification to Kafka
        sendNotification(
                "Developer Task Count",
                String.format("Developer %s has completed %d tasks in the last %d days.",
                        developerId, taskCount, sinceDays)
        );
        
        log.info("Developer {} has {} tasks in the last {} days", developerId, taskCount, sinceDays);
        
        return response;
    }

    private void validateSinceDays(int sinceDays) {
        if (sinceDays <= 0) {
            throw new BadRequestException("sinceDays must be a positive number");
        }
    }

    private void sendNotification(String subject, String body) {
        try {
            EvaluationResultMessage message = EvaluationResultMessage.builder()
                    .to(managerEmail)
                    .subject(subject)
                    .body(body)
                    .build();
            
            kafkaTemplate.send(EMAIL_TOPIC, message);
            log.info("Sent notification to topic '{}': {}", EMAIL_TOPIC, subject);
        } catch (Exception e) {
            log.error("Failed to send notification to Kafka: {}", e.getMessage(), e);
            // Don't fail the request if notification fails
        }
    }
}
