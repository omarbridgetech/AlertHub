package com.mst.processorms.dto;



import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MetricResponseDTO {
    private UUID id;
    private Integer userId;
    private String name;
    private Label label;
    private Integer threshold;
    private Integer timeFrameHours;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


