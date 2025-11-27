package com.mst.actionms.dto;

import com.mst.actionms.model.ActionType;
import com.mst.actionms.model.Runday;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;


@Getter
@Setter
public class ActionRequest {


        @NotBlank
        private String name;

        @NotBlank
        private String to;

        @NotNull
        private ActionType actionType;

        @NotNull
        private LocalTime runOnTime;

        @NotNull
        private Runday runOnDay;

        @NotBlank
        private String message;

        @NotBlank
        private String condition;


}
