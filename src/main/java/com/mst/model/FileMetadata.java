package com.mst.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata",
        uniqueConstraints = @UniqueConstraint(columnNames = {"file_name", "provider"}))
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "scanned_at", nullable = false)
    private LocalDateTime scannedAt;

    @Column(name = "records_loaded")
    private Integer recordsLoaded;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ScanStatus status;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    // Constructors
    public FileMetadata() {
    }

    public FileMetadata(Long id, String fileName, Provider provider, LocalDateTime scannedAt,
                        Integer recordsLoaded, ScanStatus status, String errorMessage) {
        this.id = id;
        this.fileName = fileName;
        this.provider = provider;
        this.scannedAt = scannedAt;
        this.recordsLoaded = recordsLoaded;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Provider getProvider() { return provider; }
    public void setProvider(Provider provider) { this.provider = provider; }

    public LocalDateTime getScannedAt() { return scannedAt; }
    public void setScannedAt(LocalDateTime scannedAt) { this.scannedAt = scannedAt; }

    public Integer getRecordsLoaded() { return recordsLoaded; }
    public void setRecordsLoaded(Integer recordsLoaded) { this.recordsLoaded = recordsLoaded; }

    public ScanStatus getStatus() { return status; }
    public void setStatus(ScanStatus status) { this.status = status; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    @PrePersist
    protected void onCreate() {
        if (scannedAt == null) {
            scannedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = ScanStatus.PENDING;
        }
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private FileMetadata metadata = new FileMetadata();

        public Builder fileName(String fileName) {
            metadata.fileName = fileName;
            return this;
        }

        public Builder provider(Provider provider) {
            metadata.provider = provider;
            return this;
        }

        public Builder scannedAt(LocalDateTime scannedAt) {
            metadata.scannedAt = scannedAt;
            return this;
        }

        public Builder recordsLoaded(Integer recordsLoaded) {
            metadata.recordsLoaded = recordsLoaded;
            return this;
        }

        public Builder status(ScanStatus status) {
            metadata.status = status;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            metadata.errorMessage = errorMessage;
            return this;
        }

        public FileMetadata build() {
            return metadata;
        }
    }
}