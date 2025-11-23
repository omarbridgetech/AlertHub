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
@Schema(description = "Label count information")
public class LabelCountDto {

    @Schema(description = "Label name", example = "bug")
    private String label;

    @Schema(description = "Count of tasks with this label", example = "10")
    private long count;
}
