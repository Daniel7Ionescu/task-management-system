package com.teamrocket.tms.services.user;

import com.teamrocket.tms.exceptions.user.UserNotFoundException;
import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.services.task.TaskService;
import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.repositories.UserRepository;
import com.teamrocket.tms.services.project.ProjectService;
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

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, UserServiceValidation userServiceValidation, ProjectService projectService, TaskService taskService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
        this.modelMapper = modelMapper;
        this.userServiceValidation = userServiceValidation;
        this.projectService = projectService;
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

        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        log.info("User {} : {} inserted in db", savedUser.getId(), savedUser.getLastName());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));;

        if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if (userDTO.getRole() != null) user.setRole(userDTO.getRole());
        if (userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());
        if (userDTO.getTeam() != null) user.setTeam(userDTO.getTeam());

        User savedUser = userRepository.save(user);
        log.info("User {} : {} updated in db", savedUser.getId(), savedUser.getLastName());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, long id) {
        User userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
      
        return taskService.createTask(taskDTO, userEntity);
    }

    @Override
    public TaskDTO getTaskById(Long userId, Long taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));
        log.info("User {} : {} tried to retrieve Task with id {} - from getTaskById", user.getId(), user.getLastName(), taskId);

        return taskService.getTaskById(taskId);
    }

    @Override
    public ProjectDTO createProject(Long userId, ProjectDTO projectDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));
        log.info("User with the id {} retrieved. From createProject", userId);
        userServiceValidation.validateUserRoleCanPerformAction(user, Role.PROJECTMANAGER);

        return projectService.createProject(projectDTO);
    }
}
