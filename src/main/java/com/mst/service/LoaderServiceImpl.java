package com.mst.service;

import com.mst.model.FileMetadata;
import com.mst.model.PlatformInformation;
import com.mst.model.Provider;
import com.mst.model.ScanStatus;
import com.mst.repository.FileMetadataRepository;
import com.mst.repository.PlatformInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LoaderServiceImpl implements LoaderService {

    private static final Logger log = LoggerFactory.getLogger(LoaderServiceImpl.class);

    private final GitHubFileFetcher gitHubFileFetcher;
    private final CsvParserService csvParserService;
    private final PlatformInformationRepository platformInfoRepository;
    private final FileMetadataRepository fileMetadataRepository;

    public LoaderServiceImpl(GitHubFileFetcher gitHubFileFetcher,
                             CsvParserService csvParserService,
                             PlatformInformationRepository platformInfoRepository,
                             FileMetadataRepository fileMetadataRepository) {
        this.gitHubFileFetcher = gitHubFileFetcher;
        this.csvParserService = csvParserService;
        this.platformInfoRepository = platformInfoRepository;
        this.fileMetadataRepository = fileMetadataRepository;
    }

    @Override
    @Transactional
    public int scanAndLoadProvider(Provider provider) {
        log.info("Starting scan for provider: {}", provider);

        try {
            List<String> scannedFiles = fileMetadataRepository.findByProvider(provider)
                    .stream()
                    .filter(fm -> fm.getStatus() == ScanStatus.SUCCESS)
                    .map(FileMetadata::getFileName)
                    .toList();

            log.info("Found {} already scanned files for provider {}", scannedFiles.size(), provider);

            List<GitHubFileFetcher.GitHubFileInfo> unscannedFiles =
                    gitHubFileFetcher.getUnscannedFiles(provider, scannedFiles);

            if (unscannedFiles.isEmpty()) {
                log.info("No new files to scan for provider: {}", provider);
                return 0;
            }

            int totalRecordsLoaded = 0;

            for (GitHubFileFetcher.GitHubFileInfo fileInfo : unscannedFiles) {
                totalRecordsLoaded += processGitHubFile(fileInfo);
            }

            log.info("Completed scan for provider: {}. Total records loaded: {}",
                    provider, totalRecordsLoaded);
            return totalRecordsLoaded;

        } catch (Exception e) {
            log.error("Error scanning provider: {}", provider, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Map<Provider, Integer> scanAndLoadAllProviders() {
        log.info("Starting scan for all providers");
        Map<Provider, Integer> results = new HashMap<>();

        for (Provider provider : Provider.values()) {
            try {
                int recordsLoaded = scanAndLoadProvider(provider);
                results.put(provider, recordsLoaded);
            } catch (Exception e) {
                log.error("Error scanning provider: {}", provider, e);
                results.put(provider, 0);
            }
        }

        log.info("Completed scan for all providers. Results: {}", results);
        return results;
    }

    @Override
    @Transactional
    public int triggerManualScan(Provider provider) {
        log.info("Manual scan triggered for provider: {}", provider);
        return scanAndLoadProvider(provider);
    }

    @Override
    @Transactional
    public Map<Provider, Integer> triggerManualScanAll() {
        log.info("Manual scan triggered for all providers");
        return scanAndLoadAllProviders();
    }

    @Transactional
    protected int processGitHubFile(GitHubFileFetcher.GitHubFileInfo fileInfo) {
        String fileName = fileInfo.fileName();
        Provider provider = fileInfo.provider();
        LocalDateTime scanTime = LocalDateTime.now();

        log.info("Processing file: {} from provider: {}", fileName, provider);

        FileMetadata fileMetadata = FileMetadata.builder()
                .fileName(fileName)
                .provider(provider)
                .scannedAt(scanTime)
                .status(ScanStatus.PROCESSING)
                .build();

        try {
            if (fileMetadataRepository.existsByFileNameAndProvider(fileName, provider)) {
                log.warn("File already processed: {}", fileName);
                return 0;
            }

            fileMetadata = fileMetadataRepository.save(fileMetadata);

            String csvContent = gitHubFileFetcher.downloadFileContent(fileInfo.downloadUrl());

            List<PlatformInformation> platformData = parseCsvData(csvContent, provider, scanTime);

            platformInfoRepository.saveAll(platformData);

            fileMetadata.setStatus(ScanStatus.SUCCESS);
            fileMetadata.setRecordsLoaded(platformData.size());
            fileMetadataRepository.save(fileMetadata);

            log.info("Successfully processed file: {}. Records loaded: {}",
                    fileName, platformData.size());

            return platformData.size();

        } catch (Exception e) {
            log.error("Error processing file: {}", fileName, e);

            fileMetadata.setStatus(ScanStatus.FAILED);
            fileMetadata.setErrorMessage(e.getMessage());
            fileMetadataRepository.save(fileMetadata);

            throw e;
        }
    }

    private List<PlatformInformation> parseCsvData(String csvContent, Provider provider,
                                                   LocalDateTime scanTime) {
        return switch (provider) {
            case GITHUB -> csvParserService.parseGitHubCsv(csvContent, scanTime);
            case JIRA -> csvParserService.parseJiraCsv(csvContent, scanTime);
            case CLICKUP -> csvParserService.parseClickUpCsv(csvContent, scanTime);
        };
    }
}
