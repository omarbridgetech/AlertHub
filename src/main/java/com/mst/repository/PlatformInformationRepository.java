package com.mst.repository;

import com.mst.model.PlatformInformation;
import com.mst.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface PlatformInformationRepository extends JpaRepository<PlatformInformation, Long>
{
    List<PlatformInformation> findByProvider(Provider provider);
    List<PlatformInformation>findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    long countByProvider(Provider provider);
}
