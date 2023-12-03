package com.teamrocket.tms.services;

import com.teamrocket.tms.models.dtos.TaskDTO;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO);
}
