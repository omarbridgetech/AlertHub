package com.Alnsor.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;
    private long expiresInMs;
    private Long userId;
    private String username;
    private boolean isAdmin;
    private List<String> permissions;
}
