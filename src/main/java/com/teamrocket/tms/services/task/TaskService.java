package com.teamrocket.tms.services.task;

import com.teamrocket.tms.models.dtos.TaskDTO;

import java.util.List;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO, long id);

    List<TaskDTO> getAllTasks();

    TaskDTO getTaskById(Long id);
}
