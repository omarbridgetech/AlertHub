package com.Alnsor.User.repository;

import com.Alnsor.User.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    
    Optional<RoleEntity> findByRole(String role);
}
