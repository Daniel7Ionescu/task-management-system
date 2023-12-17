package com.teamrocket.tms.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30, unique = true)
    private String name;

    @Column(name = "percentage_complete")
    private double percentageComplete;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Column(name = "is_done")
    private boolean isDone;

    @Column(name = "description", length = 250)
    private String description;

    @OneToOne(mappedBy = "project")
    @JsonManagedReference
    private Team team;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference(value = "projectReference")
    private List<Task> tasks = new ArrayList<>();
}