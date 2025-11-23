package com.alerthub.evaluation.controller;

import com.alerthub.evaluation.dto.DeveloperLabelAggregationResponse;
import com.alerthub.evaluation.dto.DeveloperTaskAmountResponse;
import com.alerthub.evaluation.dto.MostLabelDeveloperResponse;
import com.alerthub.evaluation.service.EvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluation")
@RequiredArgsConstructor
@Validated
@Tag(name = "Evaluation", description = "Developer evaluation and statistics API")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @Operation(
            summary = "Get developer with most occurrences of a specific label",
            description = "Finds the developer who has the highest number of tasks with the specified label within the given time frame"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved developer information",
                    content = @Content(schema = @Schema(implementation = MostLabelDeveloperResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = com.alerthub.evaluation.dto.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No developer found with the specified label",
                    content = @Content(schema = @Schema(implementation = com.alerthub.evaluation.dto.ApiError.class))
            )
    })
    @GetMapping("/developer/most-label")
    public ResponseEntity<MostLabelDeveloperResponse> getDeveloperWithMostLabel(
            @Parameter(description = "Label to search for", required = true, example = "bug")
            @RequestParam @NotBlank(message = "Label must not be blank") String label,
            
            @Parameter(description = "Number of days back from now", required = true, example = "30")
            @RequestParam("since") @Positive(message = "Since days must be positive") int sinceDays
    ) {
        MostLabelDeveloperResponse response = evaluationService.findDeveloperWithMostLabel(label, sinceDays);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get label aggregation for a specific developer",
            description = "Returns aggregated counts for each label assigned to the specified developer within the given time frame"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved label aggregation",
                    content = @Content(schema = @Schema(implementation = DeveloperLabelAggregationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = com.alerthub.evaluation.dto.ApiError.class))
            )
    })
    @GetMapping("/developer/{developerId}/label-aggregate")
    public ResponseEntity<DeveloperLabelAggregationResponse> getDeveloperLabelAggregation(
            @Parameter(description = "Developer ID", required = true, example = "DEV001")
            @PathVariable @NotBlank(message = "Developer ID must not be blank") String developerId,
            
            @Parameter(description = "Number of days back from now", required = true, example = "30")
            @RequestParam("since") @Positive(message = "Since days must be positive") int sinceDays
    ) {
        DeveloperLabelAggregationResponse response = 
                evaluationService.aggregateLabelsForDeveloper(developerId, sinceDays);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get total task count for a specific developer",
            description = "Returns the total number of tasks assigned to the specified developer within the given time frame"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved task count",
                    content = @Content(schema = @Schema(implementation = DeveloperTaskAmountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = com.alerthub.evaluation.dto.ApiError.class))
            )
    })
    @GetMapping("/developer/{developerId}/task-amount")
    public ResponseEntity<DeveloperTaskAmountResponse> getDeveloperTaskAmount(
            @Parameter(description = "Developer ID", required = true, example = "DEV001")
            @PathVariable @NotBlank(message = "Developer ID must not be blank") String developerId,
            
            @Parameter(description = "Number of days back from now", required = true, example = "30")
            @RequestParam("since") @Positive(message = "Since days must be positive") int sinceDays
    ) {
        DeveloperTaskAmountResponse response = 
                evaluationService.getTaskAmountForDeveloper(developerId, sinceDays);
        return ResponseEntity.ok(response);
    }
}
