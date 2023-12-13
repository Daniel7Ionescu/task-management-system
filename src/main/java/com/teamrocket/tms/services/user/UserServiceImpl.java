package com.teamrocket.tms.services.user;

import com.teamrocket.tms.exceptions.project.ProjectNotFoundException;
import com.teamrocket.tms.exceptions.user.UserNotFoundException;
import com.teamrocket.tms.exceptions.user.UserUnauthorizedActionException;
import com.teamrocket.tms.models.dtos.*;
import com.teamrocket.tms.models.entities.*;
import com.teamrocket.tms.repositories.UserRepository;
import com.teamrocket.tms.services.project.ProjectService;
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
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + id + " not found."));
        log.info("User with the id {} retrieved.", id);

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
        if (userDTO.getRole() == null){
            userDTO.setRole(Role.JUNIOR);
        }

        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        log.info("User {} : {} inserted in db", savedUser.getId(), savedUser.getLastName());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        User savedUser = userRepository.save(user);
        log.info("User {} : {} updated in db.", savedUser.getId(), savedUser.getLastName());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUserRole(Long userId, UserDTO userDTO, Long targetUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));
        log.info("User with the id {} retrieved. From updateUserRole.", userId);

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));
        log.info("User with the id {} retrieved. From updateUserRole.", userId);

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
        User userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));
        String userName = userEntity.getFirstName() + " " + userEntity.getLastName();
      
        return taskService.createTask(taskDTO, userName);
    }

    @Override
    public TaskDTO getTaskById(Long userId, Long taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));
        log.info("User {} : {} tried to retrieve Task with id {} - from getTaskById", user.getId(), user.getLastName(), taskId);

        return taskService.getTaskById(taskId);
    }

    @Override
    public UserDTO assignTask(Long userId, Long taskId, Long targetUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));
        log.info("User with the id {} retrieved.", userId);

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + targetUserId + " not found."));
        log.info("User with the id {} retrieved.", targetUserId);

        taskService.assignUserToTask(targetUser, taskId);

        return modelMapper.map(targetUser, UserDTO.class);
    }

    @Override
    public ProjectDTO createProject(Long userId, ProjectDTO projectDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));
        log.info("User with the id {} retrieved. From createProject", userId);
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);

        return projectService.createProject(projectDTO);
    }

    @Override
    public TeamDTO createTeam(Long userId, TeamDTO teamDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));
        log.info("User {} : {} retrieved. From createTeam.", userId, user.getLastName());
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);

        return teamService.createTeam(teamDTO);
    }

    @Override
    public TeamDTO assignTeamLeader(Long userId, Long teamId, Long targetUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));
        log.info("User {} : {} retrieved. From assignTeamLeader.", userId, user.getLastName());
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + targetUserId + " not found."));
        log.info("User {} : {} retrieved. From assignTeamLeader.", userId, user.getLastName());

        userServiceValidation.validateAreUsersEquals(user, targetUser);
        teamService.validateTeamAlreadyHasTeamLeader(teamId);
        userServiceValidation.validateUserRoleCanPerformAction(targetUser, Role.SENIOR);

        targetUser.setRole(Role.TEAM_LEADER);
        targetUser.setTeam(modelMapper.map(teamService.getTeamById(teamId), Team.class));
        User savedUser = userRepository.save(targetUser);
        log.info("User {} : {} added to the team {} set role to PM. From assignTeamLeader.", savedUser.getId(), savedUser.getLastName(), targetUser.getTeam());

        return teamService.assignTeamLeader(teamId, targetUserId);
    }

    @Override
    public TeamDTO assignProjectToTeam(Long userId, Long teamId, Long targetProjectId) {
        User userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));
        log.info("User {} : {} retrieved.", userId, userEntity.getLastName());

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
        User userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));

        userServiceValidation.validateUserRoleCanPerformAction(userEntity, Role.PROJECT_MANAGER, Role.TEAM_LEADER);

        TeamDTO teamDTO = teamService.getTeamById(teamId);
        UserDTO targetUserDTO = getUserById(targetUserId);

        userServiceValidation.validateUserAlreadyInATeam(targetUserDTO);

        Team teamEntity = modelMapper.map(teamDTO, Team.class);
        User targetUserEntity = modelMapper.map(targetUserDTO, User.class);

        targetUserEntity.setTeam(teamEntity);
        User savedUser = userRepository.save(targetUserEntity);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public void deleteProject(Long userId, Long id) {
       ProjectDTO projectDTO =  projectService.getProjectById(id);
               if (projectDTO == null) {
                   throw new ProjectNotFoundException("Project with the id " + id + "not found.");
               }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + userId + "not found."));
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECT_MANAGER);
        projectService.deleteProject(id);
        log.info("Project with id {} deleted by user with id {}.", id, userId);
    }
}
