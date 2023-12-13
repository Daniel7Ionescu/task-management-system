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

    TaskDTO createTask(TaskDTO taskDTO, Long userId);

    TaskDTO getTaskById(Long userId, Long taskId);

    UserDTO assignTask(Long userId, Long taskId, Long targetUserId);

    ProjectDTO createProject(Long userId, ProjectDTO projectDTO);

    TeamDTO createTeam(Long userId, TeamDTO teamDTO);

    TeamDTO assignTeamLeader(Long userId, Long teamId, Long leaderId);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    TeamDTO assignProjectToTeam(Long userId, Long teamId, Long targetProjectId);

    UserDTO assignUserToTeam(Long userId, Long teamId, Long targetUserId);

    void deleteProject(Long userId, Long id);
}