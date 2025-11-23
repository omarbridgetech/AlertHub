package com.alerthub.evaluation.controller;

import com.alerthub.evaluation.dto.PlatformInformationRequest;
import com.alerthub.evaluation.dto.PlatformInformationResponse;
import com.alerthub.evaluation.entity.PlatformInformation;
import com.alerthub.evaluation.repository.PlatformInformationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/platform-info")
@RequiredArgsConstructor
@Validated
@Tag(name = "Platform Information", description = "Manage platform information entries (tasks/issues)")
public class PlatformInformationController {

    private final PlatformInformationRepository platformInformationRepository;

    @Operation(
            summary = "Create a new platform information entry",
            description = "Add a new task/issue entry to the platformInformation table"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Entry created successfully",
                    content = @Content(schema = @Schema(implementation = PlatformInformationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = com.alerthub.evaluation.dto.ApiError.class))
            )
    })
    @PostMapping
    public ResponseEntity<PlatformInformationResponse> createEntry(
            @Valid @RequestBody PlatformInformationRequest request
    ) {
        PlatformInformation entity = PlatformInformation.builder()
                .timestamp(request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now())
                .ownerId(request.getOwnerId())
                .project(request.getProject())
                .tag(request.getTag())
                .label(request.getLabel())
                .developerId(request.getDeveloperId())
                .taskNumber(request.getTaskNumber())
                .env(request.getEnv())
                .userStory(request.getUserStory())
                .taskPoint(request.getTaskPoint())
                .sprint(request.getSprint())
                .build();

        PlatformInformation saved = platformInformationRepository.save(entity);

        PlatformInformationResponse response = mapToResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get all platform information entries",
            description = "Retrieve all task/issue entries from the database"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved entries"
            )
    })
    @GetMapping
    public ResponseEntity<List<PlatformInformationResponse>> getAllEntries() {
        List<PlatformInformation> entries = platformInformationRepository.findAll();
        List<PlatformInformationResponse> responses = entries.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Delete all platform information entries",
            description = "Clear all data from the platformInformation table"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "All entries deleted successfully"
            )
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteAllEntries() {
        platformInformationRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private PlatformInformationResponse mapToResponse(PlatformInformation entity) {
        return PlatformInformationResponse.builder()
                .id(entity.getId())
                .timestamp(entity.getTimestamp())
                .ownerId(entity.getOwnerId())
                .project(entity.getProject())
                .tag(entity.getTag())
                .label(entity.getLabel())
                .developerId(entity.getDeveloperId())
                .taskNumber(entity.getTaskNumber())
                .env(entity.getEnv())
                .userStory(entity.getUserStory())
                .taskPoint(entity.getTaskPoint())
                .sprint(entity.getSprint())
                .build();
    }
}
