package com.mst.repository;

import com.mst.model.FileMetadata;
import com.mst.model.Provider;
import com.mst.model.ScanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    Optional<FileMetadata> findByFileNameAndProvider(String fileName, Provider provider);

    boolean existsByFileNameAndProvider(String fileName, Provider provider);

    List<FileMetadata> findByProvider(Provider provider);

    List<FileMetadata> findByStatus(ScanStatus status);

    List<FileMetadata> findByProviderOrderByScannedAtDesc(Provider provider);
}
