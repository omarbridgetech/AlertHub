package com.Alnsor.User.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private List<RoleDto> roles;
}
