package com.teamrocket.tms.services.task;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.utils.enums.Priority;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO, String userName, Project project);

    List<TaskDTO> getAllTasks();

    List<TaskDTO> getFilteredTasks(Map<String, String> parameters, Project project);

    TaskDTO getTaskById(Long id);

    TaskDTO completeTaskObjectives(Long userId, Long taskId, TaskDTO taskDTO);

    List<TaskDTO> getAllTasksForUser(Long userId);

    TaskDTO assignUserToTask(User userEntity, Long taskId);

    void validateTaskCanBeAssigned(Task task);
}
