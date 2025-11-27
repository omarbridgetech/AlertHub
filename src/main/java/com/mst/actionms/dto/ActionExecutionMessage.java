package com.mst.actionms.dto;

import com.mst.actionms.model.ActionType;
import com.mst.actionms.model.Runday;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
