package com.mst.metricms.repository;

import com.mst.metricms.model.Label;
import com.mst.metricms.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Metric entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface MetricRepository extends JpaRepository<Metric, UUID> {

    /**
     * Find all metrics created by a specific user
     */
    List<Metric> findByUserId(Integer userId);

    /**
     * Find a metric by its unique name
     */
    Optional<Metric> findByName(String name);

    /**
     * Find all metrics with a specific label
     */
    List<Metric> findByLabel(Label label);

    /**
     * Check if a metric with the given name already exists
     */
    boolean existsByName(String name);

    /**
     * Find metrics by user ID and label
     */
    @Query("SELECT m FROM Metric m WHERE m.userId = :userId AND m.label = :label")
    List<Metric> findByUserIdAndLabel(@Param("userId") Integer userId,
                                      @Param("label") Label label);

    /**
     * Find metrics by user ID ordered by creation date descending
     */
    List<Metric> findByUserIdOrderByCreatedAtDesc(Integer userId);

    /**
     * Count metrics created by a specific user
     */
    long countByUserId(Integer userId);

    /**
     * Find metrics with threshold greater than or equal to specified value
     */
    List<Metric> findByThresholdGreaterThanEqual(Integer threshold);

    /**
     * Find metrics within a specific time frame range
     */
    @Query("SELECT m FROM Metric m WHERE m.timeFrameHours BETWEEN :minHours AND :maxHours")
    List<Metric> findByTimeFrameRange(@Param("minHours") Integer minHours,
                                      @Param("maxHours") Integer maxHours);

    Metric save(Optional<Metric> existingMetric);
}

