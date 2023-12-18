package com.teamrocket.tms.services.project;

import com.teamrocket.tms.exceptions.project.ProjectNotFoundException;
import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.repositories.ProjectRepository;
import com.teamrocket.tms.utils.calculators.CompletionCalculator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.teamrocket.tms.utils.calculators.CompletionCalculator.*;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectServiceValidation projectServiceValidation;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectServiceValidation projectServiceValidation, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.projectServiceValidation = projectServiceValidation;
        this.modelMapper = modelMapper;
    }


    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        projectServiceValidation.validateProjectAlreadyExists(projectDTO);

        Project project = modelMapper.map(projectDTO, Project.class);
        Project savedProject = projectRepository.save(project);
        log.info("Project created successfully with ID: {}", savedProject.getId());
        return modelMapper.map(savedProject, ProjectDTO.class);
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id " + id + " not found."));

        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .toList();
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public void validateProjectIsAssignable(ProjectDTO projectDTO) {
        projectServiceValidation.validateProjectIsAssignable(projectDTO);
    }

    @Override
    public void updateProjectPercentageComplete(Project project) {
        project.setPercentageComplete(getProjectPercentageComplete(project.getTasks()));

        Project savedProject = projectRepository.save(project);
        log.info("Project created successfully with ID: {}", savedProject.getId());
    }
}