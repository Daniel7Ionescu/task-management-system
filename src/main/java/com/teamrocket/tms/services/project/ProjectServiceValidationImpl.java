package com.teamrocket.tms.services.project;

import com.teamrocket.tms.exceptions.project.ProjectAlreadyExistsException;
import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.repositories.ProjectRepository;
import org.springframework.stereotype.Component;

@Component
public class ProjectServiceValidationImpl implements ProjectServiceValidation {

    private final ProjectRepository projectRepository;

    public ProjectServiceValidationImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void validateProjectAlreadyExists(ProjectDTO projectDTO) {
        Project projectEntity = projectRepository.findByName(projectDTO.getName());

        if (projectEntity != null) {
            throw new ProjectAlreadyExistsException("Project with name " + projectDTO.getName() + " already exists.");
        }
    }
}
