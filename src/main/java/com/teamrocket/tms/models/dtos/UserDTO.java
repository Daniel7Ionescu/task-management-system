package com.teamrocket.tms.models.dtos;

import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.entities.Team;
import com.teamrocket.tms.utils.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 30, message = "must be between 3 and 30 characters")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 30, message = "must be between 3 and 30 characters")
    private String lastName;

    private Role role;

    @NotBlank
    @Email
    private String email;

    private List<Task> tasksForUser = new ArrayList<>();

    private Team team;
}
