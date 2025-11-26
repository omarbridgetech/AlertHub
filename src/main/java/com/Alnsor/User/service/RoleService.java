package com.Alnsor.User.service;

import com.Alnsor.User.dto.RoleDto;
import com.Alnsor.User.entity.RoleEntity;
import com.Alnsor.User.exception.RoleNotFoundException;
import com.Alnsor.User.mapper.RoleMapper;
import com.Alnsor.User.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    public RoleDto getRoleById(Integer id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        return roleMapper.toDto(role);
    }

    public RoleDto getRoleByName(String roleName) {
        RoleEntity role = roleRepository.findByRole(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + roleName));
        return roleMapper.toDto(role);
    }
}
