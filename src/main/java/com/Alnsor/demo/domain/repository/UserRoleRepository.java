package com.Alnsor.demo.domain.repository;

import com.Alnsor.demo.domain.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);
    List<UserRole> findByUserId(Long userId);
}
