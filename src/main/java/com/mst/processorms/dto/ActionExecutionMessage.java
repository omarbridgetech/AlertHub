package com.mst.processorms.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ActionExecutionMessage {
    private UUID actionId;
    private String userId;
    private ActionType actionType;
    private String to;
    private String message;
    private String condition;
    private LocalDateTime runDateTime;
}
