package com.teamrocket.tms.models.dtos;

import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.entities.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ProjectDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 30, message = "must be between 3 and 30 characters")
    private String name;

    private double percentageComplete;

    private boolean isComplete;

    @NotBlank
    @Size(min = 3, max = 250, message = "must be between 3 and 250 characters")
    private String description;

    private Team team;

    private List<Task> tasks;
}