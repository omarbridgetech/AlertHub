package com.alerthub.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create platform information entry")
public class PlatformInformationRequest {

    @Schema(description = "Timestamp of the task", example = "2025-11-23T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Owner/Manager ID", example = "MGR001")
    private String ownerId;

    @Schema(description = "Project name", example = "AlertHub")
    private String project;

    @Schema(description = "Tag", example = "backend")
    private String tag;

    @Schema(description = "Label/Type", example = "bug")
    @NotBlank(message = "Label is required")
    private String label;

    @Schema(description = "Developer ID", example = "DEV001")
    @NotBlank(message = "Developer ID is required")
    private String developerId;

    @Schema(description = "Task number", example = "TASK-001")
    private String taskNumber;

    @Schema(description = "Environment", example = "production")
    private String env;

    @Schema(description = "User story description", example = "Fix login issue")
    private String userStory;

    @Schema(description = "Task points", example = "5")
    private Integer taskPoint;

    @Schema(description = "Sprint name", example = "Sprint-12")
    private String sprint;
}
