package com.mst.service;

import com.mst.model.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class GitHubFileFetcher {

    private static final Logger log = LoggerFactory.getLogger(GitHubFileFetcher.class);

    @Value("${loader.github.repo.owner:teamMST}")
    private String repoOwner;

    @Value("${loader.github.repo.name:MST_AlertHub}")
    private String repoName;

    @Value("${loader.github.token:#{null}}")
    private String githubToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final Pattern FILE_NAME_PATTERN =
            Pattern.compile("^(github|jira|clickup)_(\\d{4})_(\\d{2})_(\\d{2})T(\\d{2})_(\\d{2})_(\\d{2})\\.csv$");

    public GitHubFileFetcher(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private String getPathForProvider(Provider provider) {
        return switch (provider) {
            case GITHUB -> "gitHub";
            case JIRA -> "jira";
            case CLICKUP -> "clickUp";
        };
    }

    public List<GitHubFileInfo> listFilesInBranch(Provider provider) throws Exception {
        String path = getPathForProvider(provider);
        String url = String.format(
                "https://api.github.com/repos/%s/%s/contents/%s?ref=main",
                repoOwner, repoName, path
        );

        log.info("Fetching file list from GitHub: {}", url);

        try {
            var headers = new org.springframework.http.HttpHeaders();
            if (githubToken != null && !githubToken.isEmpty()) {
                headers.set("Authorization", "Bearer " + githubToken);
            }
            headers.set("Accept", "application/vnd.github.v3+json");

            var entity = new org.springframework.http.HttpEntity<>(headers);
            var response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    String.class  // Get as String first, then parse manually
            );

            String responseBody = response.getBody();
            if (responseBody == null || responseBody.isEmpty()) {
                log.warn("Empty response from GitHub API");
                return new ArrayList<>();
            }

            // Parse JSON manually
            JsonNode rootNode = objectMapper.readTree(responseBody);
            List<GitHubFileInfo> csvFiles = new ArrayList<>();

            if (rootNode.isArray()) {
                for (JsonNode fileNode : rootNode) {
                    String fileName = fileNode.get("name").asText();
                    String downloadUrl = fileNode.get("download_url").asText();
                    String sha = fileNode.get("sha").asText();
                    String type = fileNode.get("type").asText();

                    if (type.equals("file") && fileName.endsWith(".csv") && isValidFileName(fileName)) {
                        csvFiles.add(new GitHubFileInfo(fileName, downloadUrl, sha, provider));
                        log.debug("Found CSV file: {}", fileName);
                    }
                }
            }

            log.info("Found {} CSV files in path {}", csvFiles.size(), path);
            return csvFiles;

        } catch (Exception e) {
            log.error("Error fetching file list from GitHub", e);
            throw new Exception("Failed to fetch file list from GitHub: " + e.getMessage(), e);
        }
    }

    public String downloadFileContent(String downloadUrl) throws Exception {
        log.info("Downloading file from: {}", downloadUrl);

        try {
            var headers = new org.springframework.http.HttpHeaders();
            if (githubToken != null && !githubToken.isEmpty()) {
                headers.set("Authorization", "Bearer " + githubToken);
            }

            var entity = new org.springframework.http.HttpEntity<>(headers);
            var response = restTemplate.exchange(
                    downloadUrl,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    String.class
            );

            String content = response.getBody();
            if (content == null || content.isEmpty()) {
                throw new Exception("Downloaded file is empty");
            }

            log.info("Successfully downloaded file, size: {} bytes", content.length());
            return content;

        } catch (Exception e) {
            log.error("Error downloading file from GitHub", e);
            throw new Exception("Failed to download file: " + e.getMessage(), e);
        }
    }

    public List<GitHubFileInfo> getUnscannedFiles(Provider provider, List<String> scannedFileNames) throws Exception {
        List<GitHubFileInfo> allFiles = listFilesInBranch(provider);

        List<GitHubFileInfo> unscannedFiles = allFiles.stream()
                .filter(file -> !scannedFileNames.contains(file.fileName()))
                .toList();

        log.info("Found {} unscanned files for provider {}", unscannedFiles.size(), provider);
        return unscannedFiles;
    }

    public boolean isValidFileName(String fileName) {
        return FILE_NAME_PATTERN.matcher(fileName).matches();
    }

    public record GitHubFileInfo(String fileName, String downloadUrl, String sha, Provider provider) {}
}