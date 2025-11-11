package com.mst.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitHubDataDTO {
    @JsonProperty("manager_id")
    private String managerId;

    private String projects;
    private String assignee;
    private String label;

    @JsonProperty("devloper_id")
    private String developerId;

    private String issue;
    private String environment;

    @JsonProperty("user_story")
    private String userStory;

    private Integer point;
    private String sprint;
}
