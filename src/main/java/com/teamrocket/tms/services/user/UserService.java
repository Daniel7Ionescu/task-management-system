package com.teamrocket.tms.services.user;

import com.teamrocket.tms.models.dtos.*;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    UserDTO updateUserRole(Long userId, UserDTO userDTO, Long targetUserId);

    TaskDTO createTask(TaskDTO taskDTO, Long userId);

    TaskDTO getTaskById(Long userId, Long taskId);

    UserDTO assignTask(Long userId, Long taskId, Long targetUserId);

    TaskDTO userCompleteTaskObjectives(Long userId, Long taskId, TaskDTO taskDTO);

    TaskDTO reviewTask(Long userId, Long taskId, TaskDTO taskDTO);

    List<TaskDTO> getFilteredTasks(Long userId, TaskFilterDTO parameters);

    List<TaskDTO> getAllTasksForUser(Long userId);

    TaskDTO addCommentToTask(Long userId, Long taskId, String comment);

    ProjectDTO createProject(Long userId, ProjectDTO projectDTO);

    void deleteProject(Long userId, Long id);

    TeamDTO createTeam(Long userId, TeamDTO teamDTO);

    TeamDTO assignTeamLeader(Long userId, Long teamId, Long leaderId);

    TeamDTO assignProjectToTeam(Long userId, Long teamId, Long targetProjectId);

    UserDTO assignUserToTeam(Long userId, Long teamId, Long targetUserId);
}