package com.alerthub.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing aggregated label counts for a developer")
public class DeveloperLabelAggregationResponse {

    @Schema(description = "Developer ID", example = "DEV001")
    private String developerId;

    @Schema(description = "Number of days from now that were searched", example = "30")
    private int sinceDays;

    @Schema(description = "List of label counts")
    private List<LabelCountDto> labelCounts;
}
