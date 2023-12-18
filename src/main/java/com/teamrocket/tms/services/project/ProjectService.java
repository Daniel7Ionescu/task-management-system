package com.teamrocket.tms.services.project;

import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.entities.Project;

import java.util.List;

public interface ProjectService {

    ProjectDTO createProject(ProjectDTO projectDTO);

    ProjectDTO getProjectById(Long id);

    List<ProjectDTO> getAllProjects();

    void deleteProject(Long id);

    void validateProjectIsAssignable(ProjectDTO projectDTO);

    void updateProjectPercentageComplete(Project project);
}