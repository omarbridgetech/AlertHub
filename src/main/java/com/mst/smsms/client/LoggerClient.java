package com.mst.smsms.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LoggerClient {

    private final RestTemplate restTemplate;

    @Value("${logger.service.url:http://logger-app:8086}")
    private String loggerServiceUrl;

    private static final String SERVICE_NAME = "SmsMs";

    public LoggerClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Send log to Logger microservice
     */
    public void sendLog(String logLevel, String message) {
        try {
            String url = loggerServiceUrl + "/api/logs";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> logRequest = new HashMap<>();
            logRequest.put("serviceName", SERVICE_NAME);
            logRequest.put("logLevel", logLevel);
            logRequest.put("message", message);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(logRequest, headers);

            restTemplate.postForObject(url, request, String.class);
            log.debug("Log sent to Logger service: [{}] {}", logLevel, message);

        } catch (Exception e) {
            // Don't let logging failures affect the main service
            log.warn("Failed to send log to Logger service: {}", e.getMessage());
        }
    }

    // Convenience methods
    public void info(String message) {
        sendLog("INFO", message);
    }

    public void error(String message) {
        sendLog("ERROR", message);
    }

    public void warn(String message) {
        sendLog("WARN", message);
    }

    public void debug(String message) {
        sendLog("DEBUG", message);
    }
}
