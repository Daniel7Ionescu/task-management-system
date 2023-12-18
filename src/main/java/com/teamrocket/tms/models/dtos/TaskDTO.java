package com.teamrocket.tms.models.dtos;

import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.utils.enums.Priority;
import com.teamrocket.tms.utils.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class TaskDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 30, message = "must be between 3 and 30 characters")
    private String title;

    @NotBlank
    @Size(min = 3, max = 100, message = "must be between 3 and 100 characters")
    private String description;

    private String createdBy;

    private Status status;

    private Priority priority;

    private double progress;

    private boolean isObjectiveMapComplete;

    private LocalDate dueDate;

    private String completedBy;

    private String reviewedBy;

    private Map<String, String> comments = new LinkedHashMap<>();

    @NotEmpty
    private Map<String, Boolean> objectives = new HashMap<>();

    private Project project;

    private User user;
}
