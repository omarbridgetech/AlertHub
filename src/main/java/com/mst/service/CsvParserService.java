package com.mst.service;

import com.mst.model.PlatformInformation;
import com.mst.model.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvParserService {

    private static final Logger log = LoggerFactory.getLogger(CsvParserService.class);

    public List<PlatformInformation> parseGitHubCsv(String csvContent, LocalDateTime scanTime) throws Exception {
        log.info("Parsing GitHub CSV content");
        try {
            List<Map<String, String>> rows = parseCsv(csvContent);
            List<PlatformInformation> result = new ArrayList<>();

            for (Map<String, String> row : rows) {
                PlatformInformation info = PlatformInformation.builder()
                        .timestamp(scanTime)
                        .ownerId(getStringValue(row, "manager_id"))
                        .project(getStringValue(row, "projects"))
                        .tag(getStringValue(row, "assignee"))
                        .label(getStringValue(row, "label"))
                        .developerId(getStringValue(row, "developer_id"))
                        .taskNumber(getStringValue(row, "issue"))
                        .environment(getStringValue(row, "environment"))
                        .userStory(getStringValue(row, "user_story"))
                        .taskPoint(getIntegerValue(row, "point"))
                        .sprint(getStringValue(row, "sprint"))
                        .provider(Provider.GITHUB)
                        .build();
                result.add(info);
            }

            log.info("Parsed {} GitHub records", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error parsing GitHub CSV", e);
            throw new Exception("Failed to parse GitHub CSV", e);
        }
    }

    public List<PlatformInformation> parseJiraCsv(String csvContent, LocalDateTime scanTime) throws Exception {
        log.info("Parsing Jira CSV content");
        try {
            List<Map<String, String>> rows = parseCsv(csvContent);
            List<PlatformInformation> result = new ArrayList<>();

            for (Map<String, String> row : rows) {
                PlatformInformation info = PlatformInformation.builder()
                        .timestamp(scanTime)
                        .ownerId(getStringValue(row, "manager_id"))
                        .project(getStringValue(row, "projects"))
                        .tag(getStringValue(row, "assignee"))
                        .label(getStringValue(row, "label"))
                        .developerId(getStringValue(row, "employeeID"))
                        .taskNumber(getStringValue(row, "issue"))
                        .environment(getStringValue(row, "env"))
                        .userStory(getStringValue(row, "user_story"))
                        .taskPoint(getIntegerValue(row, "point"))
                        .sprint(getStringValue(row, "sprint"))
                        .provider(Provider.JIRA)
                        .build();
                result.add(info);
            }

            log.info("Parsed {} Jira records", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error parsing Jira CSV", e);
            throw new Exception("Failed to parse Jira CSV", e);
        }
    }

    public List<PlatformInformation> parseClickUpCsv(String csvContent, LocalDateTime scanTime) throws Exception {
        log.info("Parsing ClickUp CSV content");
        try {
            List<Map<String, String>> rows = parseCsv(csvContent);
            List<PlatformInformation> result = new ArrayList<>();

            for (Map<String, String> row : rows) {
                PlatformInformation info = PlatformInformation.builder()
                        .timestamp(scanTime)
                        .ownerId(getStringValue(row, "owner_id"))
                        .project(getStringValue(row, "project"))
                        .tag(getStringValue(row, "tag"))
                        .label(getStringValue(row, "label"))
                        .developerId(getStringValue(row, "worker_id"))
                        .taskNumber(getStringValue(row, "task"))
                        .environment(getStringValue(row, "pr_env"))
                        .userStory(getStringValue(row, "user_story"))
                        .taskPoint(getIntegerValue(row, "day"))
                        .sprint(getStringValue(row, "currant_sprint"))
                        .provider(Provider.CLICKUP)
                        .build();
                result.add(info);
            }

            log.info("Parsed {} ClickUp records", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error parsing ClickUp CSV", e);
            throw new Exception("Failed to parse ClickUp CSV", e);
        }
    }

    private List<Map<String, String>> parseCsv(String csvContent) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new Exception("CSV file is empty");
            }

            String[] headers = parseCsvLine(headerLine);
            log.debug("CSV Headers: {}", String.join(", ", headers));

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                if (values.length != headers.length) {
                    log.warn("Line {} has {} values but expected {}. Skipping.",
                            lineNumber, values.length, headers.length);
                    continue;
                }

                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i].trim(), values[i].trim());
                }
                result.add(row);
            }

            log.debug("Parsed {} data rows from CSV", result.size());
        } catch (Exception e) {
            throw new Exception("Error parsing CSV content", e);
        }

        return result;
    }

    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString());

        return values.toArray(new String[0]);
    }

    private String getStringValue(Map<String, String> row, String key) {
        String value = row.get(key);
        return (value == null || value.isEmpty()) ? null : value;
    }

    private Integer getIntegerValue(Map<String, String> row, String key) {
        String value = row.get(key);
        if (value == null || value.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid integer value '{}' for key '{}', using 0", value, key);
            return 0;
        }
    }
}