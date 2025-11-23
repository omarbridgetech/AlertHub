package com.alerthub.evaluation.repository;

import com.alerthub.evaluation.entity.PlatformInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface PlatformInformationRepository extends JpaRepository<PlatformInformation, Long> {

    /**
     * Count tasks for a specific developer and label within a time range
     */
    @Query("SELECT COUNT(p) FROM PlatformInformation p " +
           "WHERE p.developerId = :developerId " +
           "AND p.label = :label " +
           "AND p.timestamp BETWEEN :fromDate AND :toDate")
    long countByDeveloperAndLabel(
            @Param("developerId") String developerId,
            @Param("label") String label,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    /**
     * Find developer with most tasks for a specific label within a time range
     */
    @Query("SELECT p.developerId, COUNT(p) as taskCount " +
           "FROM PlatformInformation p " +
           "WHERE p.label = :label " +
           "AND p.timestamp BETWEEN :fromDate AND :toDate " +
           "GROUP BY p.developerId " +
           "ORDER BY taskCount DESC")
    List<Object[]> findDeveloperWithMostTasksByLabel(
            @Param("label") String label,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    /**
     * Aggregate label counts for a specific developer within a time range
     */
    @Query("SELECT p.label, COUNT(p) as labelCount " +
           "FROM PlatformInformation p " +
           "WHERE p.developerId = :developerId " +
           "AND p.timestamp BETWEEN :fromDate AND :toDate " +
           "GROUP BY p.label")
    List<Object[]> aggregateLabelsByDeveloper(
            @Param("developerId") String developerId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    /**
     * Count total tasks for a specific developer within a time range
     */
    @Query("SELECT COUNT(p) FROM PlatformInformation p " +
           "WHERE p.developerId = :developerId " +
           "AND p.timestamp BETWEEN :fromDate AND :toDate")
    long countTasksByDeveloper(
            @Param("developerId") String developerId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
}
