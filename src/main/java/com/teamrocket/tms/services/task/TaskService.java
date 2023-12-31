package com.teamrocket.tms.services.task;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.entities.User;

import java.util.List;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO, String userName);

    List<TaskDTO> getAllTasks();

    TaskDTO getTaskById(Long id);

    Task updateTask(Task task);

    List<TaskDTO> getAllTasksForUser(Long userId);

    TaskDTO assignUserToTask(User userEntity, Long taskId);

    void validateTaskCanBeAssigned(Task task);
}
