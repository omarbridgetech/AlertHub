package com.Alnsor.demo.service;

import com.Alnsor.demo.domain.entity.Role;
import com.Alnsor.demo.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }
}
