package com.Alnsor.User.mapper;

import com.Alnsor.User.dto.UserDto;
import com.Alnsor.User.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setRoles(entity.getUserRoles().stream()
                .map(userRole -> roleMapper.toDto(userRole.getRole()))
                .collect(Collectors.toList()));

        return dto;
    }
}
