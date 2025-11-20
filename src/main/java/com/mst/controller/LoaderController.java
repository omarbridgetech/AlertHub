package com.mst.controller;

import com.mst.model.Provider;
import com.mst.service.LoaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/loader")
@Tag(
        name = "Loader Controller",
        description = "Endpoints for triggering manual scans and loading data from GitHub repositories (GitHub, Jira, ClickUp)"
)
public class LoaderController {

    private static final Logger log = LoggerFactory.getLogger(LoaderController.class);
    //@Autowired
    private final LoaderService loaderService;

    public LoaderController(LoaderService loaderService) {
        this.loaderService = loaderService;
    }

    @PostMapping("/scan/manual")
    @Operation(
            summary = "Trigger manual scan for all providers",
            description = "Manually triggers a scan and load operation for all providers (GitHub, Jira, and ClickUp). " +
                    "This endpoint fetches new CSV files from the GitHub repository, parses them, and stores the data " +
                    "in the platform_information table. Files that have already been successfully scanned are skipped.",
            tags = {"Loader Controller"}
    )
    public ResponseEntity<Map<String, Object>> triggerManualScanAll() {
        log.info("Manual scan triggered for all providers");

        try {
            Map<Provider, Integer> results = loaderService.triggerManualScanAll();

            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Manual scan completed successfully");
            response.put("results", results);

            int totalRecords = results.values().stream().mapToInt(Integer::intValue).sum();
            response.put("totalRecordsLoaded", totalRecords);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error during manual scan", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("error", "Scan failed");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/scan/manual/{provider}")
    @Operation(
            summary = "Trigger manual scan for a specific provider",
            description = "Manually triggers a scan and load operation for a single provider (github, jira, or clickup). " +
                    "This endpoint fetches new CSV files for the specified provider from the GitHub repository, " +
                    "parses them, and stores the data in the platform_information table. " +
                    "Files that have already been successfully scanned are skipped.",
            tags = {"Loader Controller"}
    )
    public ResponseEntity<Map<String, Object>> triggerManualScanProvider(@PathVariable String provider) {
        log.info("Manual scan triggered for provider: {}", provider);

        try {
            Provider providerEnum = Provider.fromString(provider.toLowerCase());
            int recordsLoaded = loaderService.triggerManualScan(providerEnum);

            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Manual scan completed successfully");
            response.put("provider", providerEnum);
            response.put("recordsLoaded", recordsLoaded);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("Invalid provider: {}", provider);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("error", "Invalid provider");
            errorResponse.put("message", "Provider must be one of: github, jira, clickup");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            log.error("Error during manual scan for provider: {}", provider, e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("error", "Scan failed");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/status")
    @Operation(
            summary = "Get loader service status",
            description = "Returns the current status of the Loader service along with supported providers and timestamp. " +
                    "This endpoint can be used for health monitoring and service discovery.",
            tags = {"Loader Controller"}
    )
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "Loader Service");
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("providers", Provider.values());

        return ResponseEntity.ok(status);
    }

    @GetMapping("/health")
    @Operation(
            summary = "Health check endpoint",
            description = "Simple health check endpoint that returns the service status and current timestamp. " +
                    "Used for Kubernetes liveness/readiness probes and monitoring systems.",
            tags = {"Loader Controller"}
    )
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(health);
    }
}