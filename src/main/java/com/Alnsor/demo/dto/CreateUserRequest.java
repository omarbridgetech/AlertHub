package com.Alnsor.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;
    @NotBlank
    @Email
    private String email;
    private String phone;
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
    private boolean isAdmin;
}
