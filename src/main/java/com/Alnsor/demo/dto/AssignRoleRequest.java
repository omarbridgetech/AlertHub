package com.Alnsor.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignRoleRequest {
    @NotBlank
    private String role; // role name to assign
}
