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
@Schema(description = "Response containing developer with most tasks for a specific label")
public class MostLabelDeveloperResponse {

    @Schema(description = "Developer ID", example = "DEV001")
    private String developerId;

    @Schema(description = "Label name", example = "bug")
    private String label;

    @Schema(description = "Number of tasks with this label", example = "15")
    private long count;

    @Schema(description = "Number of days from now that were searched", example = "30")
    private int sinceDays;
}
