package com.mst.metricms.service;

import com.mst.metricms.dto.MetricRequestDTO;
import com.mst.metricms.dto.MetricResponseDTO;
import com.mst.metricms.model.Label;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Metric operations.
 * Defines business logic methods for managing metrics.
 */
public interface MetricService {

    /**
     * Create a new metric
     * @param request Metric creation request
     * @return Created metric response
     */
    MetricResponseDTO createMetric(MetricRequestDTO request);

    /**
     * Update an existing metric
     * @param id Metric ID
     * @param request Updated metric data
     * @return Updated metric response
     */
    MetricResponseDTO updateMetric(UUID id, MetricRequestDTO request);

    /**
     * Delete a metric by ID
     * @param id Metric ID
     */
    void deleteMetric(UUID id);

    /**
     * Get a metric by ID
     * @param id Metric ID
     * @return Metric response
     */
    MetricResponseDTO getMetricById(UUID id);

    /**
     * Get all metrics
     * @return List of all metrics
     */
    List<MetricResponseDTO> getAllMetrics();

    /**
     * Get all metrics created by a specific user
     * @param userId User ID
     * @return List of metrics for the user
     */
    List<MetricResponseDTO> getMetricsByUserId(Integer userId);

    /**
     * Get all metrics with a specific label
     * @param label Label type
     * @return List of metrics with the label
     */
    List<MetricResponseDTO> getMetricsByLabel(Label label);

    /**
     * Get metrics by user ID and label
     * @param userId User ID
     * @param label Label type
     * @return List of metrics matching both criteria
     */
    List<MetricResponseDTO> getMetricsByUserIdAndLabel(Integer userId, Label label);

    /**
     * Check if a metric name already exists
     * @param name Metric name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
}
