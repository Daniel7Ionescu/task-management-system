package com.teamrocket.tms.models.entities;

import com.teamrocket.tms.utils.enums.Priority;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "priority")
    private Priority priority;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Transient
    private Map<String, String> comments = new HashMap<>();
}
