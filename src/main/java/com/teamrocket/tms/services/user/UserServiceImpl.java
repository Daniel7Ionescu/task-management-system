package com.teamrocket.tms.services.user;

import com.teamrocket.tms.exceptions.project.ProjectNotFoundException;
import com.teamrocket.tms.exceptions.user.UserDoesNotHaveATeamException;
import com.teamrocket.tms.exceptions.user.UserUnauthorizedActionException;
import com.teamrocket.tms.models.dtos.*;
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
        log.info("User {} : {} updated in db.", savedUser.getId(), savedUser.getLastName());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUserRole(Long userId, UserDTO userDTO, Long targetUserId) {
        User user = userServiceValidation.getValidUser(userId, "updateUserRole");
        User targetUser = userServiceValidation.getValidUser(targetUserId, "updateUserRole");

        if (user.getRole().getId() <= targetUser.getRole().getId()) {
            throw new UserUnauthorizedActionException("Based on user role, the action cannot be performed.");
        }

        Role role = userDTO.getRole();

        if (user.getRole().getId() <= role.getId()) {
            throw new UserUnauthorizedActionException("Based on user role, the action cannot be performed.");
        }

        targetUser.setRole(role);
        User savedUser = userRepository.save(targetUser);
        log.info("User {} : {} inserted in db.", savedUser.getId(), savedUser.getLastName());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        User userEntity = userServiceValidation.getValidUser(userId, "createTask");
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
        userServiceValidation.validateUserNotInATeam(userDTO);

        return taskService.createTask(taskDTO, userDTO, userEntity.getTeam().getProject());
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

        taskService.assignUserToTask(targetUser, taskId);

        return modelMapper.map(targetUser, UserDTO.class);
    }

    @Override
    public TaskDTO userCompleteTaskObjectives(Long userId, Long taskId, TaskDTO taskDTO) {
        return taskService.completeTaskObjectives(userId, taskId, taskDTO);
    }

    @Override
    public List<TaskDTO> getFilteredTasks(Long userId, TaskFilterDTO parameters) {
        User user = userServiceValidation.getValidUser(userId, "getAllTasksForUser");
        log.info("User with the id {} retrieved.", userId);

        if (user.getTeam() == null) {
            throw new UserDoesNotHaveATeamException("User is not part of a Team.");
        }

        return taskService.getFilteredTasks(parameters, user.getTeam().getProject());
    }

    @Override
    public TaskDTO reviewTask(Long userId, Long taskId, TaskDTO taskDTO) {
        User user = userServiceValidation.getValidUser(userId, "reviewTask");
        String reviewerName = user.getFirstName() + " " + user.getLastName();

        return taskService.userReviewTask(reviewerName, taskId, taskDTO);
    }

    @Override
    public List<TaskDTO> getAllTasksForUser(Long userId) {
        userServiceValidation.getValidUser(userId, "getAllTasksForUser");
        log.info("User with the id {} retrieved.", userId);

        return taskService.getAllTasksForUser(userId);
    }

    @Override
    public TaskDTO addCommentToTask(Long userId, Long taskId, String comment) {
        User user = userServiceValidation.getValidUser(userId, "addCommentToTask");
        String userName = user.getFirstName() + " " + user.getLastName();

        return taskService.addCommentToTask(userName, taskId, comment);
    }

    @Override
    public ProjectDTO createProject(Long userId, ProjectDTO projectDTO) {
        User user = userServiceValidation.getValidUser(userId, "createProject");
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);

        return projectService.createProject(projectDTO);
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

    @Override
    public TeamDTO createTeam(Long userId, TeamDTO teamDTO) {
        User user = userServiceValidation.getValidUser(userId, "createTeam");
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);

        return teamService.createTeam(teamDTO);
    }

    @Override
    public TeamDTO assignTeamLeader(Long userId, Long teamId, Long targetUserId) {
        User user = userServiceValidation.getValidUser(userId, "assignTeamLeader");
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);

        User targetUser = userServiceValidation.getValidUser(targetUserId, "assignTeamLeader");
        userServiceValidation.validateAreUsersEquals(user, targetUser);
        userServiceValidation.validateUserRoleCanPerformAction(targetUser, Role.SENIOR);

        teamService.validateTeamAlreadyHasTeamLeader(teamId);

        targetUser.setRole(Role.TEAM_LEADER);
        targetUser.setTeam(modelMapper.map(teamService.getTeamById(teamId), Team.class));
        User savedUser = userRepository.save(targetUser);
        log.info("User {} : {} added to the team {} set role to PM. From assignTeamLeader.", savedUser.getId(), savedUser.getLastName(), targetUser.getTeam());

        return teamService.assignTeamLeader(teamId, targetUserId);
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
    public UserDTO assignUserToTeam(Long userId, Long teamId, Long targetUserId) {
        User userEntity = userServiceValidation.getValidUser(userId, "assignUserToTeam");
        userServiceValidation.validateUserRoleCanPerformAction(userEntity, Role.PROJECT_MANAGER, Role.TEAM_LEADER);

        UserDTO targetUserDTO = getUserById(targetUserId);
        userServiceValidation.validateUserAlreadyInATeam(targetUserDTO);
        User targetUserEntity = modelMapper.map(targetUserDTO, User.class);

        TeamDTO teamDTO = teamService.getTeamById(teamId);
        Team teamEntity = modelMapper.map(teamDTO, Team.class);

        targetUserEntity.setTeam(teamEntity);
        User savedUser = userRepository.save(targetUserEntity);

        return modelMapper.map(savedUser, UserDTO.class);
    }
}