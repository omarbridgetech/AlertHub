package com.mst.actionms.repository;

import com.mst.actionms.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ActionRepository extends JpaRepository<Action, UUID> {



    List<Action> findByUserIdAndIsDeletedFalse(String userId);
    Optional<Action> findByIdAndIsDeletedFalse(UUID id);
    List<Action> findByIsDeletedFalseAndIsEnabledTrue();



}
