package com.mst.actionms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "action")
@Getter @Setter
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(name = "owner_id", nullable = false, length = 128)
    private String userId;

    @NotBlank
    @Column(nullable = false, length = 128)
    private String name;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @NotBlank
    @Column(name = "`to`", nullable = false, length = 256)
    private String to;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 8)
    private ActionType actionType;

    @NotNull
    @Column(name = "run_on_time", nullable = false)
    private LocalTime runOnTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "run_on_day", nullable = false, length = 10)
    private Runday runOnDay;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;


    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @Column(name = "last_run")
    private Timestamp lastRun;
    @NotBlank
    @Column(name = "condition_json", columnDefinition = "TEXT", nullable = false)
    private String condition;



}
