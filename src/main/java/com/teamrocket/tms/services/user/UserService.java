package com.teamrocket.tms.services.user;

import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.dtos.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    ProjectDTO createProject(Long userId, ProjectDTO projectDTO);
}
