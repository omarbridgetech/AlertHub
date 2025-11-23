package com.mst.loogerms.repository;
import com.mst.loogerms.model.LogEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface LogRepository extends MongoRepository<LogEntry, String> {

    List<LogEntry> findByServiceName(String serviceName);

    List<LogEntry> findByLogLevel(String logLevel);

    List<LogEntry> findByServiceNameAndLogLevel(String serviceName, String logLevel);

    List<LogEntry> findByTimestampAfter(LocalDateTime timestamp);

    List<LogEntry> findByServiceNameAndTimestampAfter(String serviceName, LocalDateTime timestamp);

    List<LogEntry> findByServiceNameOrderByTimestampDesc(String serviceName);

    List<LogEntry> findAllByOrderByTimestampDesc();
}
