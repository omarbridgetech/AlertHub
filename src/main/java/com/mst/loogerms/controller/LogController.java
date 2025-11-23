package com.mst.loogerms.controller;


import com.mst.loogerms.model.LogEntry;
import com.mst.loogerms.model.LogRequest;
import com.mst.loogerms.service.LogService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@Slf4j
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping
    public ResponseEntity<LogEntry> createLog( @RequestBody LogRequest request) {
        log.info("Received log request from: {}", request.getServiceName());
        LogEntry created = logService.createLog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<LogEntry>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }

    @GetMapping("/service/{serviceName}")
    public ResponseEntity<List<LogEntry>> getLogsByServiceName(
             @PathVariable String serviceName) {
        return ResponseEntity.ok(logService.getLogsByServiceName(serviceName));
    }



    public ResponseEntity<List<LogEntry>> getLogsByServiceNameAndLogLevel(
            @PathVariable String serviceName,
            @PathVariable String logLevel) {
        return ResponseEntity.ok(logService.getLogsByServiceNameAndLogLevel(serviceName, logLevel.toUpperCase()));
    }



    @GetMapping("/service/{serviceName}/since/{hours}")
    public ResponseEntity<List<LogEntry>> getLogsByServiceNameSince(
            @PathVariable String serviceName,
            @PathVariable int hours) {
        return ResponseEntity.ok(logService.getLogsByServiceNameSince(serviceName, hours));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Logger service is running");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllLogs() {
        logService.deleteAllLogs();
        return ResponseEntity.ok("All logs deleted");
    }
}
