package com.teamrocket.tms.models.entities;

import com.teamrocket.tms.utils.enums.Priority;
import com.teamrocket.tms.utils.enums.Status;
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

    @Column(name = "title", length = 30, nullable = false, unique = true)
    private String title;

    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "status")
    private Status status;

    @Column(name = "priority")
    private Priority priority;

    @Column(name = "progress")
    private double progress;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "completed_by")
    private String completedBy;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

//    @ElementCollection
//    @CollectionTable(name = "comment")
//    @Column(name = "comments")
    @Transient
    private Map<String, String> comments = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "objective_completion",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @MapKeyColumn(name = "objective_name")
    @Column(name = "is_complete")
    private Map<String, Boolean> objectives = new HashMap<>();
}
