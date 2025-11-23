package com.mst.loogerms.service;


import com.mst.loogerms.model.LogEntry;
import com.mst.loogerms.model.LogRequest;
import com.mst.loogerms.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public LogEntry createLog(LogRequest request) {
        LogEntry logEntry = LogEntry.builder()
                .timestamp(LocalDateTime.now())
                .serviceName(request.getServiceName())
                .logLevel(request.getLogLevel())
                .message(request.getMessage())
                .build();

        LogEntry saved = logRepository.save(logEntry);
        log.info("Log created: [{}] {} - {}", saved.getLogLevel(), saved.getServiceName(), saved.getMessage());
        return saved;
    }

    public List<LogEntry> getAllLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }

    public List<LogEntry> getLogsByServiceName(String serviceName) {
        return logRepository.findByServiceNameOrderByTimestampDesc(serviceName);
    }

    public List<LogEntry> getLogsByLogLevel(String logLevel) {
        return logRepository.findByLogLevel(logLevel);
    }

    public List<LogEntry> getLogsByServiceNameAndLogLevel(String serviceName, String logLevel) {
        return logRepository.findByServiceNameAndLogLevel(serviceName, logLevel);
    }

    public List<LogEntry> getLogsSince(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return logRepository.findByTimestampAfter(since);
    }

    public List<LogEntry> getLogsByServiceNameSince(String serviceName, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return logRepository.findByServiceNameAndTimestampAfter(serviceName, since);
    }

    public void deleteAllLogs() {
        logRepository.deleteAll();
        log.warn("All logs deleted");
    }
}
