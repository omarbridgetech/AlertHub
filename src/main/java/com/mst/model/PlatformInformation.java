package com.mst.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Entity
@Table(name="platform_information")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private LocalDateTime timestamp;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "project")
    private String project;

    @Column(name = "tag")
    private String tag;

    @Column(name = "label")
    private String label;

    @Column(name = "developer_id")
    private String developerId;

    @Column(name = "task_number")
    private String taskNumber;

    private String environment;

    @Column(name = "user_story")
    private String userStory;

    @Column(name = "task_point ")
    private Integer taskPoint;

    private  String sprint;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private Provider provider;

    //to be executed automatically before a new entity is first saved into the db
    @PrePersist
    public void prePersist() {
        if(timestamp==null){
        timestamp = LocalDateTime.now();
    }
        if(taskPoint==null){
            taskPoint = 0;
        }

    }









}
