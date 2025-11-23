package com.alerthub.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing total task count for a developer")
public class DeveloperTaskAmountResponse {

    @Schema(description = "Developer ID", example = "DEV001")
    private String developerId;

    @Schema(description = "Number of days from now that were searched", example = "30")
    private int sinceDays;

    @Schema(description = "Total number of tasks", example = "42")
    private long taskCount;
}
