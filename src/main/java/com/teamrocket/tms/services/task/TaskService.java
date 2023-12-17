package com.teamrocket.tms.services.task;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.utils.enums.Priority;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO, String userName);

    List<TaskDTO> getAllTasks();

    List<TaskDTO> getFilteredTasks(Map<String, String> parameters);

    TaskDTO getTaskById(Long id);

    Task updateTask(Task task);

    List<TaskDTO> getAllTasksForUser(Long userId);

    TaskDTO assignUserToTask(User userEntity, Long taskId);

    void validateTaskCanBeAssigned(Task task);
}
