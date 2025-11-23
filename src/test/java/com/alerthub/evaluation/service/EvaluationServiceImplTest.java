package com.alerthub.evaluation.service;

import com.alerthub.evaluation.dto.*;
import com.alerthub.evaluation.exception.BadRequestException;
import com.alerthub.evaluation.exception.ResourceNotFoundException;
import com.alerthub.evaluation.repository.PlatformInformationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceImplTest {

    @Mock
    private PlatformInformationRepository platformInformationRepository;

    @Mock
    private KafkaTemplate<String, EvaluationResultMessage> kafkaTemplate;

    @InjectMocks
    private EvaluationServiceImpl evaluationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(evaluationService, "managerEmail", "test@example.com");
    }

    @Test
    void findDeveloperWithMostLabel_Success() {
        // Arrange
        String label = "bug";
        int sinceDays = 30;
        String developerId = "DEV001";
        Long count = 15L;

        Object[] result = new Object[]{developerId, count};
        List<Object[]> results = Collections.singletonList(result);

        when(platformInformationRepository.findDeveloperWithMostTasksByLabel(
                eq(label), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(results);

        // Act
        MostLabelDeveloperResponse response = evaluationService.findDeveloperWithMostLabel(label, sinceDays);

        // Assert
        assertNotNull(response);
        assertEquals(developerId, response.getDeveloperId());
        assertEquals(label, response.getLabel());
        assertEquals(count, response.getCount());
        assertEquals(sinceDays, response.getSinceDays());

        verify(platformInformationRepository, times(1))
                .findDeveloperWithMostTasksByLabel(eq(label), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(kafkaTemplate, times(1)).send(eq("email"), any(EvaluationResultMessage.class));
    }

    @Test
    void findDeveloperWithMostLabel_NoResults_ThrowsResourceNotFoundException() {
        // Arrange
        String label = "nonexistent";
        int sinceDays = 30;

        when(platformInformationRepository.findDeveloperWithMostTasksByLabel(
                eq(label), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> evaluationService.findDeveloperWithMostLabel(label, sinceDays));

        assertTrue(exception.getMessage().contains(label));
        assertTrue(exception.getMessage().contains(String.valueOf(sinceDays)));

        verify(platformInformationRepository, times(1))
                .findDeveloperWithMostTasksByLabel(eq(label), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void findDeveloperWithMostLabel_InvalidSinceDays_ThrowsBadRequestException() {
        // Arrange
        String label = "bug";
        int sinceDays = -5;

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> evaluationService.findDeveloperWithMostLabel(label, sinceDays));

        assertTrue(exception.getMessage().contains("positive"));

        verify(platformInformationRepository, never())
                .findDeveloperWithMostTasksByLabel(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void aggregateLabelsForDeveloper_Success() {
        // Arrange
        String developerId = "DEV001";
        int sinceDays = 30;

        Object[] result1 = new Object[]{"bug", 10L};
        Object[] result2 = new Object[]{"feature", 15L};
        List<Object[]> results = Arrays.asList(result1, result2);

        when(platformInformationRepository.aggregateLabelsByDeveloper(
                eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(results);

        // Act
        DeveloperLabelAggregationResponse response = 
                evaluationService.aggregateLabelsForDeveloper(developerId, sinceDays);

        // Assert
        assertNotNull(response);
        assertEquals(developerId, response.getDeveloperId());
        assertEquals(sinceDays, response.getSinceDays());
        assertEquals(2, response.getLabelCounts().size());

        LabelCountDto labelCount1 = response.getLabelCounts().get(0);
        assertEquals("bug", labelCount1.getLabel());
        assertEquals(10L, labelCount1.getCount());

        LabelCountDto labelCount2 = response.getLabelCounts().get(1);
        assertEquals("feature", labelCount2.getLabel());
        assertEquals(15L, labelCount2.getCount());

        verify(platformInformationRepository, times(1))
                .aggregateLabelsByDeveloper(eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(kafkaTemplate, times(1)).send(eq("email"), any(EvaluationResultMessage.class));
    }

    @Test
    void aggregateLabelsForDeveloper_NoResults_ReturnsEmptyList() {
        // Arrange
        String developerId = "DEV999";
        int sinceDays = 30;

        when(platformInformationRepository.aggregateLabelsByDeveloper(
                eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // Act
        DeveloperLabelAggregationResponse response = 
                evaluationService.aggregateLabelsForDeveloper(developerId, sinceDays);

        // Assert
        assertNotNull(response);
        assertEquals(developerId, response.getDeveloperId());
        assertEquals(sinceDays, response.getSinceDays());
        assertTrue(response.getLabelCounts().isEmpty());

        verify(platformInformationRepository, times(1))
                .aggregateLabelsByDeveloper(eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(kafkaTemplate, times(1)).send(eq("email"), any(EvaluationResultMessage.class));
    }

    @Test
    void getTaskAmountForDeveloper_Success() {
        // Arrange
        String developerId = "DEV001";
        int sinceDays = 30;
        long taskCount = 42L;

        when(platformInformationRepository.countTasksByDeveloper(
                eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(taskCount);

        // Act
        DeveloperTaskAmountResponse response = 
                evaluationService.getTaskAmountForDeveloper(developerId, sinceDays);

        // Assert
        assertNotNull(response);
        assertEquals(developerId, response.getDeveloperId());
        assertEquals(sinceDays, response.getSinceDays());
        assertEquals(taskCount, response.getTaskCount());

        verify(platformInformationRepository, times(1))
                .countTasksByDeveloper(eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(kafkaTemplate, times(1)).send(eq("email"), any(EvaluationResultMessage.class));
    }

    @Test
    void getTaskAmountForDeveloper_ZeroTasks_ReturnsZero() {
        // Arrange
        String developerId = "DEV999";
        int sinceDays = 30;
        long taskCount = 0L;

        when(platformInformationRepository.countTasksByDeveloper(
                eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(taskCount);

        // Act
        DeveloperTaskAmountResponse response = 
                evaluationService.getTaskAmountForDeveloper(developerId, sinceDays);

        // Assert
        assertNotNull(response);
        assertEquals(developerId, response.getDeveloperId());
        assertEquals(sinceDays, response.getSinceDays());
        assertEquals(0L, response.getTaskCount());

        verify(platformInformationRepository, times(1))
                .countTasksByDeveloper(eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(kafkaTemplate, times(1)).send(eq("email"), any(EvaluationResultMessage.class));
    }

    @Test
    void getTaskAmountForDeveloper_InvalidSinceDays_ThrowsBadRequestException() {
        // Arrange
        String developerId = "DEV001";
        int sinceDays = 0;

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> evaluationService.getTaskAmountForDeveloper(developerId, sinceDays));

        assertTrue(exception.getMessage().contains("positive"));

        verify(platformInformationRepository, never())
                .countTasksByDeveloper(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void verifyKafkaMessageContent_FindDeveloperWithMostLabel() {
        // Arrange
        String label = "bug";
        int sinceDays = 30;
        String developerId = "DEV001";
        Long count = 15L;

        Object[] result = new Object[]{developerId, count};
        List<Object[]> results = Collections.singletonList(result);

        when(platformInformationRepository.findDeveloperWithMostTasksByLabel(
                eq(label), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(results);

        ArgumentCaptor<EvaluationResultMessage> messageCaptor = 
                ArgumentCaptor.forClass(EvaluationResultMessage.class);

        // Act
        evaluationService.findDeveloperWithMostLabel(label, sinceDays);

        // Assert
        verify(kafkaTemplate).send(eq("email"), messageCaptor.capture());
        EvaluationResultMessage capturedMessage = messageCaptor.getValue();

        assertNotNull(capturedMessage);
        assertEquals("test@example.com", capturedMessage.getTo());
        assertTrue(capturedMessage.getSubject().contains(label));
        assertTrue(capturedMessage.getBody().contains(developerId));
        assertTrue(capturedMessage.getBody().contains(String.valueOf(count)));
    }

    @Test
    void verifyKafkaMessageContent_AggregateLabelsForDeveloper() {
        // Arrange
        String developerId = "DEV001";
        int sinceDays = 30;

        Object[] result1 = new Object[]{"bug", 10L};
        List<Object[]> results = Collections.singletonList(result1);

        when(platformInformationRepository.aggregateLabelsByDeveloper(
                eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(results);

        ArgumentCaptor<EvaluationResultMessage> messageCaptor = 
                ArgumentCaptor.forClass(EvaluationResultMessage.class);

        // Act
        evaluationService.aggregateLabelsForDeveloper(developerId, sinceDays);

        // Assert
        verify(kafkaTemplate).send(eq("email"), messageCaptor.capture());
        EvaluationResultMessage capturedMessage = messageCaptor.getValue();

        assertNotNull(capturedMessage);
        assertEquals("test@example.com", capturedMessage.getTo());
        assertTrue(capturedMessage.getSubject().contains("Label Aggregation"));
        assertTrue(capturedMessage.getBody().contains(developerId));
        assertTrue(capturedMessage.getBody().contains("bug"));
    }

    @Test
    void verifyKafkaMessageContent_GetTaskAmountForDeveloper() {
        // Arrange
        String developerId = "DEV001";
        int sinceDays = 30;
        long taskCount = 42L;

        when(platformInformationRepository.countTasksByDeveloper(
                eq(developerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(taskCount);

        ArgumentCaptor<EvaluationResultMessage> messageCaptor = 
                ArgumentCaptor.forClass(EvaluationResultMessage.class);

        // Act
        evaluationService.getTaskAmountForDeveloper(developerId, sinceDays);

        // Assert
        verify(kafkaTemplate).send(eq("email"), messageCaptor.capture());
        EvaluationResultMessage capturedMessage = messageCaptor.getValue();

        assertNotNull(capturedMessage);
        assertEquals("test@example.com", capturedMessage.getTo());
        assertTrue(capturedMessage.getSubject().contains("Task Count"));
        assertTrue(capturedMessage.getBody().contains(developerId));
        assertTrue(capturedMessage.getBody().contains(String.valueOf(taskCount)));
    }
}
