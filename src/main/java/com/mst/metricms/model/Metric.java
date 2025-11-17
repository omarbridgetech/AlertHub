package com.mst.metricms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Metric entity representing a metric condition for monitoring tasks.
 *
 * Example: If there are 10 tasks labeled as 'bug' within the last 12 hours,
 * the metric condition is met.
 */
@Entity
@Table(name = "metric")
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User ID is required")
    private Integer userId;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 50)
    @NotNull(message = "Label is required")
    private Label label;

    @Column(name = "threshold", nullable = false)
    @NotNull(message = "Threshold is required")
    @Min(value = 1, message = "Threshold must be at least 1")
    private Integer threshold;

    @Column(name = "time_frame_hours", nullable = false)
    @NotNull(message = "Time frame is required")
    @Min(value = 1, message = "Time frame must be at least 1 hour")
    @Max(value = 24, message = "Time frame must be at most 24 hours")
    private Integer timeFrameHours;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Metric() {
    }

    public Metric(Integer userId, String name, Label label, Integer threshold, Integer timeFrameHours) {
        this.userId = userId;
        this.name = name;
        this.label = label;
        this.threshold = threshold;
        this.timeFrameHours = timeFrameHours;
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

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Metric metric = new Metric();

        public Builder userId(Integer userId) {
            metric.userId = userId;
            return this;
        }

        public Builder name(String name) {
            metric.name = name;
            return this;
        }

        public Builder label(Label label) {
            metric.label = label;
            return this;
        }

        public Builder threshold(Integer threshold) {
            metric.threshold = threshold;
            return this;
        }

        public Builder timeFrameHours(Integer timeFrameHours) {
            metric.timeFrameHours = timeFrameHours;
            return this;
        }

        public Metric build() {
            return metric;
        }
    }

    @Override
    public String toString() {
        return "Metric{" +
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