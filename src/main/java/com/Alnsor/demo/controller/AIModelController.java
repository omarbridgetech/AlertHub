package com.Alnsor.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai/models")
public class AIModelController {

    @Value("${app.ai.enabledModels:}")
    private String enabledModels;

    @GetMapping
    public ResponseEntity<List<String>> listEnabledModels() {
        if (enabledModels == null || enabledModels.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        List<String> list = Arrays.stream(enabledModels.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
