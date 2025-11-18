package com.mst.metricms.service;

import com.mst.metricms.dto.MetricRequestDTO;
import com.mst.metricms.dto.MetricResponseDTO;
//import com.mst.metricms.exception.DuplicateMetricException;
//import com.mst.metricms.exception.MetricNotFoundException;
import com.mst.metricms.model.Label;
import com.mst.metricms.model.Metric;
import com.mst.metricms.repository.MetricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of MetricService.
 * Contains business logic for metric operations.
 */
@Service
@Transactional
public class MetricServiceImpl implements MetricService {

    private static final Logger log = LoggerFactory.getLogger(MetricServiceImpl.class);

    private final MetricRepository metricRepository;

    public MetricServiceImpl(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public MetricResponseDTO createMetric(MetricRequestDTO request) {
        log.info("Creating metric with name: {}", request.getName());

        // Check if metric name already exists
        if (metricRepository.existsByName(request.getName())) {
            log.error("Metric with name '{}' already exists", request.getName());
            throw new RuntimeException(request.getName());
        }

        // Create metric entity
        Metric metric = Metric.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .label(request.getLabel())
                .threshold(request.getThreshold())
                .timeFrameHours(request.getTimeFrameHours())
                .build();

        // Save to database
        Metric savedMetric = metricRepository.save(metric);
        log.info("Metric created successfully with ID: {}", savedMetric.getId());

        return MetricResponseDTO.fromEntity(savedMetric);
    }

    @Override
    public MetricResponseDTO updateMetric(UUID id, MetricRequestDTO request) {
        log.info("Updating metric with ID: {}", id);

        // Find existing metric
        Optional<Metric> existingMetric = metricRepository.findById(id);
        return null;

    }

    @Override
    public void deleteMetric(UUID id) {
        log.info("Deleting metric with ID: {}", id);

        // Check if metric exists
        if (!metricRepository.existsById(id)) {
            log.error("Metric not found with ID: {}", id);
            throw new RuntimeException();
        }

        metricRepository.deleteById(id);
        log.info("Metric deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public MetricResponseDTO getMetricById(UUID id) {
        log.info("Fetching metric with ID: {}", id);

        Optional<Metric> metric = metricRepository.findById(id);


        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetricResponseDTO> getAllMetrics() {
        log.info("Fetching all metrics");

        List<Metric> metrics = metricRepository.findAll();
        log.info("Found {} metrics", metrics.size());

        return metrics.stream()
                .map(MetricResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetricResponseDTO> getMetricsByUserId(Integer userId) {
        log.info("Fetching metrics for user ID: {}", userId);

        List<Metric> metrics = metricRepository.findByUserIdOrderByCreatedAtDesc(userId);
        log.info("Found {} metrics for user ID: {}", metrics.size(), userId);

        return metrics.stream()
                .map(MetricResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetricResponseDTO> getMetricsByLabel(Label label) {
        log.info("Fetching metrics with label: {}", label);

        List<Metric> metrics = metricRepository.findByLabel(label);
        log.info("Found {} metrics with label: {}", metrics.size(), label);

        return metrics.stream()
                .map(MetricResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetricResponseDTO> getMetricsByUserIdAndLabel(Integer userId, Label label) {
        log.info("Fetching metrics for user ID: {} and label: {}", userId, label);

        List<Metric> metrics = metricRepository.findByUserIdAndLabel(userId, label);
        log.info("Found {} metrics for user ID: {} and label: {}", metrics.size(), userId, label);

        return metrics.stream()
                .map(MetricResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return metricRepository.existsByName(name);
    }
}
