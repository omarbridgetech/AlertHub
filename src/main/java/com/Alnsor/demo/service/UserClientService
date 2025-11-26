package com.Alnsor.demo.service;

import com.Alnsor.demo.dto.UserDto;
import com.Alnsor.demo.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserClientService {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    public UserClientService(RestTemplate restTemplate, 
                            @Value("${user.service.url:http://localhost:8081}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    /**
     * Get user details by user ID from User microservice
     */
    public UserDto getUserById(Long userId) {
        try {
            String url = userServiceUrl + "/users/" + userId;
            log.info("Fetching user from User service: {}", url);
            
            ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully retrieved user: {}", response.getBody().getUsername());
                return response.getBody();
            }
            
            throw new NotFoundException("User not found with id: " + userId);
        } catch (RestClientException e) {
            log.error("Error calling User service: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch user from User service", e);
        }
    }

    /**
     * Get user by username from User microservice
     */
    public UserDto getUserByUsername(String username) {
        try {
            String url = userServiceUrl + "/users/search/username?username=" + username;
            log.info("Fetching user by username from User service: {}", url);
            
            ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            
            throw new NotFoundException("User not found with username: " + username);
        } catch (RestClientException e) {
            log.error("Error calling User service: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch user from User service", e);
        }
    }

    /**
     * Get all users from User microservice
     */
    public List<UserDto> getAllUsers() {
        try {
            String url = userServiceUrl + "/users";
            log.info("Fetching all users from User service: {}", url);
            
            ResponseEntity<UserDto[]> response = restTemplate.getForEntity(url, UserDto[].class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
            
            return List.of();
        } catch (RestClientException e) {
            log.error("Error calling User service: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch users from User service", e);
        }
    }

    /**
     * Check if User service is available
     */
    public boolean isUserServiceAvailable() {
        try {
            String url = userServiceUrl + "/actuator/health";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            log.warn("User service is not available: {}", e.getMessage());
            return false;
        }
    }
}