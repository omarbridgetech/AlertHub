package com.mst.metricms.dto;

import com.mst.metricms.model.Label;
import jakarta.validation.constraints.*;

/**
 * DTO for creating or updating a Metric.
 * Used for incoming API requests.
 */
public class MetricRequestDTO {

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;

    @NotNull(message = "Label is required")
    private Label label;

    @NotNull(message = "Threshold is required")
    @Min(value = 1, message = "Threshold must be at least 1")
    private Integer threshold;

    @NotNull(message = "Time frame is required")
    @Min(value = 1, message = "Time frame must be at least 1 hour")
    @Max(value = 24, message = "Time frame must be at most 24 hours")
    private Integer timeFrameHours;

    // Constructors
    public MetricRequestDTO() {
    }

    public MetricRequestDTO(Integer userId, String name, Label label, Integer threshold, Integer timeFrameHours) {
        this.userId = userId;
        this.name = name;
        this.label = label;
        this.threshold = threshold;
        this.timeFrameHours = timeFrameHours;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "MetricRequestDTO{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", label=" + label +
                ", threshold=" + threshold +
                ", timeFrameHours=" + timeFrameHours +
                '}';
    }
}
