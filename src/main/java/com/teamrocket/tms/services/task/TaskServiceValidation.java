package com.teamrocket.tms.services.task;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Task;

public interface TaskServiceValidation {

    void validateTaskAlreadyExists(TaskDTO taskDTO);
    void validateTaskCanBeAssigned(Task task);
}
