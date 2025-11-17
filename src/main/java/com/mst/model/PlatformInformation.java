package com.mst.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_information")
public class PlatformInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "project")
    private String project;

    @Column(name = "tag")
    private String tag;

    @Column(name = "label")
    private String label;

    @Column(name = "developer_id")
    private String developerId;

    @Column(name = "task_number")
    private String taskNumber;

    @Column(name = "environment")
    private String environment;

    @Column(name = "user_story", columnDefinition = "TEXT")
    private String userStory;

    @Column(name = "task_point")
    private Integer taskPoint;

    @Column(name = "sprint")
    private String sprint;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private Provider provider;

    // Constructors
    public PlatformInformation() {
    }

    public PlatformInformation(Long id, LocalDateTime timestamp, String ownerId, String project,
                               String tag, String label, String developerId, String taskNumber,
                               String environment, String userStory, Integer taskPoint,
                               String sprint, Provider provider) {
        this.id = id;
        this.timestamp = timestamp;
        this.ownerId = ownerId;
        this.project = project;
        this.tag = tag;
        this.label = label;
        this.developerId = developerId;
        this.taskNumber = taskNumber;
        this.environment = environment;
        this.userStory = userStory;
        this.taskPoint = taskPoint;
        this.sprint = sprint;
        this.provider = provider;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getProject() { return project; }
    public void setProject(String project) { this.project = project; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getDeveloperId() { return developerId; }
    public void setDeveloperId(String developerId) { this.developerId = developerId; }

    public String getTaskNumber() { return taskNumber; }
    public void setTaskNumber(String taskNumber) { this.taskNumber = taskNumber; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getUserStory() { return userStory; }
    public void setUserStory(String userStory) { this.userStory = userStory; }

    public Integer getTaskPoint() { return taskPoint; }
    public void setTaskPoint(Integer taskPoint) { this.taskPoint = taskPoint; }

    public String getSprint() { return sprint; }
    public void setSprint(String sprint) { this.sprint = sprint; }

    public Provider getProvider() { return provider; }
    public void setProvider(Provider provider) { this.provider = provider; }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (taskPoint == null) {
            taskPoint = 0;
        }
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PlatformInformation info = new PlatformInformation();

        public Builder timestamp(LocalDateTime timestamp) {
            info.timestamp = timestamp;
            return this;
        }

        public Builder ownerId(String ownerId) {
            info.ownerId = ownerId;
            return this;
        }

        public Builder project(String project) {
            info.project = project;
            return this;
        }

        public Builder tag(String tag) {
            info.tag = tag;
            return this;
        }

        public Builder label(String label) {
            info.label = label;
            return this;
        }

        public Builder developerId(String developerId) {
            info.developerId = developerId;
            return this;
        }

        public Builder taskNumber(String taskNumber) {
            info.taskNumber = taskNumber;
            return this;
        }

        public Builder environment(String environment) {
            info.environment = environment;
            return this;
        }

        public Builder userStory(String userStory) {
            info.userStory = userStory;
            return this;
        }

        public Builder taskPoint(Integer taskPoint) {
            info.taskPoint = taskPoint;
            return this;
        }

        public Builder sprint(String sprint) {
            info.sprint = sprint;
            return this;
        }

        public Builder provider(Provider provider) {
            info.provider = provider;
            return this;
        }

        public PlatformInformation build() {
            return info;
        }
    }
}