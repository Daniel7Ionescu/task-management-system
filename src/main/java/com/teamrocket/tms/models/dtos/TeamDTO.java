package com.teamrocket.tms.models.dtos;

import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.models.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class TeamDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 30, message = "must be between 3 and 30 characters")
    private String name;

    private Long teamLeaderId;

    private Project project;

    private List<User> userList;
}
