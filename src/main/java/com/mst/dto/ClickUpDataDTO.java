package com.mst.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClickUpDataDTO {
    @JsonProperty("owner_id")
    private String ownerId;

    private String project;
    private String tag;
    private String label;

    @JsonProperty("worker_id")
    private String workerId;

    private String task;

    @JsonProperty("pr_env")
    private String prEnv;

    @JsonProperty("user_story")
    private String userStory;

    private Integer day;

    @JsonProperty("currant_sprint")
    private String currantSprint;
}
