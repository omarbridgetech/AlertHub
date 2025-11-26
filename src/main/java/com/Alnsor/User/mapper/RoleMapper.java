package com.Alnsor.User.mapper;

import com.Alnsor.User.dto.RoleDto;
import com.Alnsor.User.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDto toDto(RoleEntity entity) {
        if (entity == null) {
            return null;
        }

        RoleDto dto = new RoleDto();
        dto.setId(entity.getId());
        dto.setRole(entity.getRole());

        return dto;
    }
}
