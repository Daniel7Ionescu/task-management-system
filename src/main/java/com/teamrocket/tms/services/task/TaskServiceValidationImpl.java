package com.teamrocket.tms.services.task;

import com.teamrocket.tms.exceptions.task.TaskAlreadyExistsException;
import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.repositories.TaskRepository;
import org.springframework.stereotype.Component;

@Component
public class TaskServiceValidationImpl implements TaskServiceValidation {

    private final TaskRepository taskRepository;

    public TaskServiceValidationImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void validateTaskAlreadyExists(TaskDTO taskDTO) {
        Task foundTask = taskRepository.findByTitle(taskDTO.getTitle());
        if (foundTask != null && foundTask.getId() == taskDTO.getId()) {
            throw new TaskAlreadyExistsException("A task with title " + taskDTO.getTitle() + " and Id " + taskDTO.getId() + " already exists.");
        }
    }
}
