package com.Alnsor.User.repository;

import com.Alnsor.User.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {
    
    Optional<UserRoleEntity> findByUserIdAndRoleId(Integer userId, Integer roleId);
    
    void deleteByUserIdAndRoleId(Integer userId, Integer roleId);
}
