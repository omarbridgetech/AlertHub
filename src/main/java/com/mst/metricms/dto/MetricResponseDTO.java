package com.mst.metricms.dto;

import com.mst.metricms.model.Label;
import com.mst.metricms.model.Metric;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Metric responses.
 * Used for outgoing API responses.
 */
public class MetricResponseDTO {

    private UUID id;
    private Integer userId;
    private String name;
    private Label label;
    private Integer threshold;
    private Integer timeFrameHours;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public MetricResponseDTO() {
    }

    public MetricResponseDTO(UUID id, Integer userId, String name, Label label,
                             Integer threshold, Integer timeFrameHours,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.label = label;
        this.threshold = threshold;
        this.timeFrameHours = timeFrameHours;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Factory method to create DTO from Entity
     */
    public static MetricResponseDTO fromEntity(Metric metric) {
        return new MetricResponseDTO(
                metric.getId(),
                metric.getUserId(),
                metric.getName(),
                metric.getLabel(),
                metric.getThreshold(),
                metric.getTimeFrameHours(),
                metric.getCreatedAt(),
                metric.getUpdatedAt()
        );
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getTimeFrameHours() {
        return timeFrameHours;
    }

    public void setTimeFrameHours(Integer timeFrameHours) {
        this.timeFrameHours = timeFrameHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "MetricResponseDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", label=" + label +
                ", threshold=" + threshold +
                ", timeFrameHours=" + timeFrameHours +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
