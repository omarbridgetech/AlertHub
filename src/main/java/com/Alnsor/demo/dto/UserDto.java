package com.Alnsor.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private boolean isAdmin;
    private List<String> permissions;
}
