package com.teamrocket.tms.services.task;

import com.teamrocket.tms.models.dtos.TaskDTO;

public interface TaskServiceValidation {

    void validateTaskAlreadyExists(TaskDTO taskDTO);
}
