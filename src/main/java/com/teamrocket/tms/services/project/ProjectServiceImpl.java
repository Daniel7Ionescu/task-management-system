package com.teamrocket.tms.services.project;

import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.repositories.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = modelMapper.map(projectDTO, Project.class);
        Project savedProject = projectRepository.save(project);
        return modelMapper.map(savedProject, ProjectDTO.class);
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found."));
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}