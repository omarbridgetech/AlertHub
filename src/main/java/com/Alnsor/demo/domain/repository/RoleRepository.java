package com.Alnsor.demo.domain.repository;

import com.Alnsor.demo.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String role);
    boolean existsByRole(String role);
}
