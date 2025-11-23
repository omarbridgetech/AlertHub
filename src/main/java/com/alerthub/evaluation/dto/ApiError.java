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
@Schema(description = "Standard error response")
public class ApiError {

    @Schema(description = "Error message", example = "Resource not found")
    private String message;

    @Schema(description = "Error type", example = "Not Found")
    private String error;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Timestamp of the error")
    private LocalDateTime timestamp;
}
