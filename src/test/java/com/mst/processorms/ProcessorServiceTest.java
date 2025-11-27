package com.mst.processorms;

import com.mst.processorms.client.LoaderClient;
import com.mst.processorms.client.MetricsClient;
import com.mst.processorms.dto.ActionExecutionMessage;
import com.mst.processorms.dto.MetricResponseDTO;
import com.mst.processorms.kafka.NotificationKafkaProducer;
import com.mst.processorms.dto.ActionType;
import com.mst.processorms.dto.Label;
import com.mst.processorms.service.ProcessorService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProcessorServiceTest {

    @Test
    void whenConditionSatisfied_shouldSendNotification() throws Exception {
        // Arrange
        MetricsClient metricsClient = mock(MetricsClient.class);
        LoaderClient loaderClient = mock(LoaderClient.class);
        NotificationKafkaProducer notificationKafkaProducer = mock(NotificationKafkaProducer.class);

        ProcessorService processorService =
                new ProcessorService(metricsClient, loaderClient, notificationKafkaProducer);

        // metric mock
        UUID metricId = UUID.randomUUID();
        MetricResponseDTO metric = new MetricResponseDTO();
        metric.setId(metricId);
        metric.setLabel(Label.BUG);
        metric.setThreshold(3);
        metric.setTimeFrameHours(12);
        when(metricsClient.getMetricDetails(metricId.toString())).thenReturn(metric);

        // loader mock – מחזיר true (threshold מתקיים)
        when(loaderClient.checkLabelThreshold(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(true);

        // action message – תנאי עם מטריקה אחת: [[metricId]]
        ActionExecutionMessage msg = new ActionExecutionMessage();
        msg.setActionId(UUID.randomUUID());
        msg.setUserId("owner-1");
        msg.setActionType(ActionType.EMAIL);
        msg.setTo("test@example.com");
        msg.setMessage("Hello");
        msg.setRunDateTime(LocalDateTime.now());
        msg.setCondition("[[\"" + metricId + "\"]]");

        // Act
        processorService.processAction(msg);

        // Assert – וידוא שנשלחה נוטיפיקציה
        verify(notificationKafkaProducer, times(1)).sendNotification(msg);
    }

    @Test
    void whenConditionNotSatisfied_shouldNotSendNotification() throws Exception {
        MetricsClient metricsClient = mock(MetricsClient.class);
        LoaderClient loaderClient = mock(LoaderClient.class);
        NotificationKafkaProducer notificationKafkaProducer = mock(NotificationKafkaProducer.class);

        ProcessorService processorService =
                new ProcessorService(metricsClient, loaderClient, notificationKafkaProducer);

        UUID metricId = UUID.randomUUID();
        MetricResponseDTO metric = new MetricResponseDTO();
        metric.setId(metricId);
        metric.setLabel(Label.BUG);
        metric.setThreshold(10);
        metric.setTimeFrameHours(12);
        when(metricsClient.getMetricDetails(metricId.toString())).thenReturn(metric);

        // loader מחזיר false (אין מספיק תקלות)
        when(loaderClient.checkLabelThreshold(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(false);

        ActionExecutionMessage msg = new ActionExecutionMessage();
        msg.setActionId(UUID.randomUUID());
        msg.setUserId("owner-1");
        msg.setActionType(ActionType.SMS);
        msg.setTo("0500000000");
        msg.setMessage("Hi");
        msg.setRunDateTime(LocalDateTime.now());
        msg.setCondition("[[\"" + metricId + "\"]]");

        processorService.processAction(msg);

        verify(notificationKafkaProducer, never()).sendNotification(any());
    }
}
