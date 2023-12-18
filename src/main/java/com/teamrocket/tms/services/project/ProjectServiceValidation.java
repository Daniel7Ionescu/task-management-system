package com.teamrocket.tms.services.project;

import com.teamrocket.tms.models.dtos.ProjectDTO;

public interface ProjectServiceValidation {

    void validateProjectAlreadyExists(ProjectDTO projectDTO);

    void validateProjectIsAssignable(ProjectDTO projectDTO);
}