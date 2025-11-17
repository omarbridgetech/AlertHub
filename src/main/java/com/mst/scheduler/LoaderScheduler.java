package com.mst.scheduler;

import com.mst.model.Provider;
import com.mst.service.LoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(name = "loader.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class LoaderScheduler {

    private static final Logger log = LoggerFactory.getLogger(LoaderScheduler.class);
    private final LoaderService loaderService;

    public LoaderScheduler(LoaderService loaderService) {
        this.loaderService = loaderService;
    }

    @Scheduled(cron = "${loader.scheduler.cron:0 0 * * * ?}")
    public void scheduledScanAllProviders() {
        log.info("=== Starting scheduled scan for all providers ===");

        try {
            Map<Provider, Integer> results = loaderService.scanAndLoadAllProviders();

            int totalRecords = results.values().stream().mapToInt(Integer::intValue).sum();

            log.info("=== Scheduled scan completed ===");
            log.info("GitHub: {} records", results.getOrDefault(Provider.GITHUB, 0));
            log.info("Jira: {} records", results.getOrDefault(Provider.JIRA, 0));
            log.info("ClickUp: {} records", results.getOrDefault(Provider.CLICKUP, 0));
            log.info("Total: {} records loaded", totalRecords);

        } catch (Exception e) {
            log.error("Error during scheduled scan", e);
        }
    }
}