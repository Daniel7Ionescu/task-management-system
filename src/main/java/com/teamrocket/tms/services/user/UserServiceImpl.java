package com.teamrocket.tms.services.user;

import com.teamrocket.tms.exceptions.project.ProjectNotFoundException;
import com.teamrocket.tms.exceptions.user.UserNotFoundException;
import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.models.entities.Team;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.repositories.UserRepository;
import com.teamrocket.tms.services.project.ProjectService;
import com.teamrocket.tms.services.task.TaskService;
import com.teamrocket.tms.services.team.TeamService;
import com.teamrocket.tms.utils.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    private final UserServiceValidation userServiceValidation;
    private final ProjectService projectService;

    private final TeamService teamService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, UserServiceValidation userServiceValidation, ProjectService projectService, TaskService taskService, TeamService teamService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
        this.modelMapper = modelMapper;
        this.userServiceValidation = userServiceValidation;
        this.projectService = projectService;
        this.teamService = teamService;

    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userServiceValidation.getValidUser(userId, "getUserById");

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        userList.stream()
                .forEach(element -> userDTOList.add(modelMapper.map(element, UserDTO.class)));
        log.info("User list retrieved.");

        return userDTOList;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userServiceValidation.validateUserAlreadyExists(userDTO);
        if (userDTO.getRole() == null) {
            userDTO.setRole(Role.JUNIOR);
        }

        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        log.info("User {} : {} inserted in db", savedUser.getId(), savedUser.getLastName());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userServiceValidation.getValidUser(userId, "updateUser");

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        User savedUser = userRepository.save(user);
        log.info("User {} : {} updated in db", savedUser.getId(), savedUser.getLastName());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        User userEntity = userServiceValidation.getValidUser(userId, "createTask");
        String userName = userEntity.getFirstName() + " " + userEntity.getLastName();

        return taskService.createTask(taskDTO, userName);
    }

    @Override
    public TaskDTO getTaskById(Long userId, Long taskId) {
        userServiceValidation.getValidUser(userId, "getTaskById");

        return taskService.getTaskById(taskId);
    }

    @Override
    public UserDTO assignTask(Long userId, Long taskId, Long targetUserId) {
        userServiceValidation.getValidUser(userId, "assignTask");
        User targetUser = userServiceValidation.getValidUser(targetUserId, "assignTask");

        TaskDTO taskDTO = taskService.getTaskById(taskId);
        Task task = modelMapper.map(taskDTO, Task.class);
        taskService.validateTaskCanBeAssigned(task);

        task.setUser(targetUser);
        taskService.updateTask(task);

        return modelMapper.map(targetUser, UserDTO.class);
    }

    @Override
    public ProjectDTO createProject(Long userId, ProjectDTO projectDTO) {
        User user = userServiceValidation.getValidUser(userId, "createProject");
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);

        return projectService.createProject(projectDTO);
    }

    @Override
    public TeamDTO createTeam(Long userId, TeamDTO teamDTO) {
        User user = userServiceValidation.getValidUser(userId, "createTeam");
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);

        return teamService.createTeam(teamDTO);
    }

    @Override
    public TeamDTO assignProjectToTeam(Long userId, Long teamId, Long targetProjectId) {
        User userEntity = userServiceValidation.getValidUser(userId, "assignProjectToTeam");
        userServiceValidation.validateUserRoleCanPerformAction(userEntity, Role.PROJECT_MANAGER);

        ProjectDTO projectDTO = projectService.getProjectById(targetProjectId);
        TeamDTO teamDTO = teamService.getTeamById(teamId);

        projectService.validateProjectIsAssignable(projectDTO);
        teamService.validateTeamIsAssignable(teamDTO);

        Project projectEntity = modelMapper.map(projectDTO, Project.class);
        Team teamEntity = modelMapper.map(teamDTO, Team.class);

        teamEntity.setProject(projectEntity);
        teamService.updateTeam(teamEntity);

        return modelMapper.map(teamEntity, TeamDTO.class);
    }

    @Override
    public void deleteProject(Long userId, Long id) {
        ProjectDTO projectDTO = projectService.getProjectById(id);
        if (projectDTO == null) {
            throw new ProjectNotFoundException("Project with the id " + id + "not found.");
        }

        User user = userServiceValidation.getValidUser(userId, "deleteProject");
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);

        projectService.deleteProject(id);
        log.info("Project with id {} deleted by user with id {}.", id, userId);
    }


}