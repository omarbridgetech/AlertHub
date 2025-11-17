package com.mst.metricms.controller;

import com.mst.metricms.dto.MetricRequestDTO;
import com.mst.metricms.dto.MetricResponseDTO;
import com.mst.metricms.model.Label;
import com.mst.metricms.service.MetricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/metrics")
@Tag(name = "Metrics", description = "API for managing metrics in the Alert Hub system")
public class MetricController {

    private static final Logger log = LoggerFactory.getLogger(MetricController.class);

    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    /**
     * Create a new metric
     * POST /api/metrics/create
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new metric",
            description = "Creates a new metric with specified threshold and time frame")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Metric created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Metric name already exists")
    })
    public ResponseEntity<MetricResponseDTO> createMetric(
            @Valid @RequestBody MetricRequestDTO request) {
        log.info("POST /api/metrics/create - Creating metric: {}", request.getName());
        MetricResponseDTO response = metricService.createMetric(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all metrics
     * GET /api/metrics/get-all
     */
    @GetMapping("/get-all")
    @Operation(summary = "Get all metrics",
            description = "Retrieves all metrics in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all metrics")
    public ResponseEntity<List<MetricResponseDTO>> getAllMetrics() {
        log.info("GET /api/metrics/get-all - Fetching all metrics");
        List<MetricResponseDTO> metrics = metricService.getAllMetrics();
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get metric by ID
     * GET /api/metrics/get/{id}
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "Get metric by ID",
            description = "Retrieves a specific metric by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metric found"),
            @ApiResponse(responseCode = "404", description = "Metric not found")
    })
    public ResponseEntity<MetricResponseDTO> getMetricById(
            @Parameter(description = "Metric ID") @PathVariable UUID id) {
        log.info("GET /api/metrics/get/{} - Fetching metric", id);
        MetricResponseDTO metric = metricService.getMetricById(id);
        return ResponseEntity.ok(metric);
    }

    /**
     * Get metrics by user ID
     * GET /api/metrics/get-by-user/{userId}
     */
    @GetMapping("/get-by-user/{userId}")
    @Operation(summary = "Get metrics by user ID",
            description = "Retrieves all metrics created by a specific user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user metrics")
    public ResponseEntity<List<MetricResponseDTO>> getMetricsByUserId(
            @Parameter(description = "User ID") @PathVariable Integer userId) {
        log.info("GET /api/metrics/get-by-user/{} - Fetching metrics for user", userId);
        List<MetricResponseDTO> metrics = metricService.getMetricsByUserId(userId);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get metrics by label
     * GET /api/metrics/get-by-label/{label}
     */
    @GetMapping("/get-by-label/{label}")
    @Operation(summary = "Get metrics by label",
            description = "Retrieves all metrics with a specific label type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved metrics"),
            @ApiResponse(responseCode = "400", description = "Invalid label")
    })
    public ResponseEntity<List<MetricResponseDTO>> getMetricsByLabel(
            @Parameter(description = "Label type (bug, enhancement, etc.)") @PathVariable String label) {
        log.info("GET /api/metrics/get-by-label/{} - Fetching metrics by label", label);
        Label labelEnum = Label.fromString(label);
        List<MetricResponseDTO> metrics = metricService.getMetricsByLabel(labelEnum);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get metrics by user ID and label
     * GET /api/metrics/get-by-user-and-label/{userId}/{label}
     */
    @GetMapping("/get-by-user-and-label/{userId}/{label}")
    @Operation(summary = "Get metrics by user and label",
            description = "Retrieves metrics for a specific user with a specific label")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved metrics")
    public ResponseEntity<List<MetricResponseDTO>> getMetricsByUserIdAndLabel(
            @Parameter(description = "User ID") @PathVariable Integer userId,
            @Parameter(description = "Label type") @PathVariable String label) {
        log.info("GET /api/metrics/get-by-user-and-label/{}/{} - Fetching metrics", userId, label);
        Label labelEnum = Label.fromString(label);
        List<MetricResponseDTO> metrics = metricService.getMetricsByUserIdAndLabel(userId, labelEnum);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Update metric
     * PUT /api/metrics/update/{id}
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Update a metric",
            description = "Updates an existing metric with new values")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metric updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Metric not found"),
            @ApiResponse(responseCode = "409", description = "Metric name already exists")
    })
    public ResponseEntity<MetricResponseDTO> updateMetric(
            @Parameter(description = "Metric ID") @PathVariable UUID id,
            @Valid @RequestBody MetricRequestDTO request) {
        log.info("PUT /api/metrics/update/{} - Updating metric", id);
        MetricResponseDTO response = metricService.updateMetric(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete metric
     * DELETE /api/metrics/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a metric",
            description = "Deletes a metric from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Metric deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Metric not found")
    })
    public ResponseEntity<Void> deleteMetric(
            @Parameter(description = "Metric ID") @PathVariable UUID id) {
        log.info("DELETE /api/metrics/delete/{} - Deleting metric", id);
        metricService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Health check endpoint
     * GET /api/metrics/health
     */
    @GetMapping("/health")
    @Operation(summary = "Health check",
            description = "Check if the Metrics service is running")
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Metrics service is running");
    }
}
