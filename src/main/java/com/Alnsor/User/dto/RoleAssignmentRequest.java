package com.Alnsor.User.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAssignmentRequest {
    
    @NotNull(message = "Role ID is required")
    private Integer roleId;
}
