package com.teamrocket.tms.services.task;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.dtos.TaskFilterDTO;
import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.entities.User;

import java.util.List;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO, UserDTO userDTO, Project project);

    List<TaskDTO> getAllTasks();

    List<TaskDTO> getFilteredTasks(TaskFilterDTO parameters, Project project);

    TaskDTO getTaskById(Long id);

    TaskDTO completeTaskObjectives(Long userId, Long taskId, TaskDTO taskDTO);

    List<TaskDTO> getAllTasksForUser(Long userId);

    TaskDTO assignUserToTask(User userEntity, Long taskId);

    void validateTaskCanBeAssigned(Task task);

    TaskDTO userReviewTask(String reviewerName, Long taskId, TaskDTO taskDTO);

    TaskDTO addCommentToTask(String userName, Long taskId, String comment);
}
