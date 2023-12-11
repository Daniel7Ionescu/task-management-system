package com.teamrocket.tms.services.user;


import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.models.dtos.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    TaskDTO createTask(TaskDTO taskDTO, long id);

    TaskDTO getTaskById(Long userId, Long taskId);

    ProjectDTO createProject(Long userId, ProjectDTO projectDTO);

    TeamDTO createTeam(Long userId, TeamDTO teamDTO);

    TeamDTO assignTeamLeader(Long userId, Long teamId, Long leaderId);
}