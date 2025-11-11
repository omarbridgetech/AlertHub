package com.mst.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JiraDataDTO {
    @JsonProperty("manager_id")
    private String managerId;

    private String projects;
    private String assignee;
    private String label;

    @JsonProperty("employeeID")
    private String employeeId;

    private String issue;
    private String env;

    @JsonProperty("user_story")
    private String userStory;

    private Integer point;
    private String sprint;
}
