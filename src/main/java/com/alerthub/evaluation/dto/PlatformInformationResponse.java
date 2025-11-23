package com.alerthub.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Platform information entry response")
public class PlatformInformationResponse {

    @Schema(description = "Entry ID", example = "1")
    private Long id;

    @Schema(description = "Timestamp")
    private LocalDateTime timestamp;

    @Schema(description = "Owner ID")
    private String ownerId;

    @Schema(description = "Project")
    private String project;

    @Schema(description = "Tag")
    private String tag;

    @Schema(description = "Label")
    private String label;

    @Schema(description = "Developer ID")
    private String developerId;

    @Schema(description = "Task number")
    private String taskNumber;

    @Schema(description = "Environment")
    private String env;

    @Schema(description = "User story")
    private String userStory;

    @Schema(description = "Task points")
    private Integer taskPoint;

    @Schema(description = "Sprint")
    private String sprint;
}
