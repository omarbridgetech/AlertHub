package com.mst.processorms.kafka;



import com.mst.processorms.dto.ActionExecutionMessage;
import com.mst.processorms.service.ProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionProcessorListener {

    private final ProcessorService processorService;

    @KafkaListener(
            topics = "${alert.actions.topic}",
            groupId = "processor-group"
    )
    public void onActionMessage(ActionExecutionMessage message) {
        log.info("Received ActionExecutionMessage from Kafka: {}", message);
        processorService.processAction(message);
    }
}
