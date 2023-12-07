package com.teamrocket.tms.services.user;


import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.dtos.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);


    TaskDTO createTask(TaskDTO taskDTO, long id);
    TaskDTO getTaskById(Long userId, Long taskId);
    UserDTO assignTask(Long userId, Long taskId, Long targetUserId);
    ProjectDTO createProject(Long userId, ProjectDTO projectDTO);
    UserDTO updateUser(Long userId, UserDTO userDTO);
}