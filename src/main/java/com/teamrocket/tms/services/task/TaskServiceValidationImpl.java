package com.teamrocket.tms.services.task;

import com.teamrocket.tms.exceptions.task.InvalidUserCompletesTaskObjective;
import com.teamrocket.tms.exceptions.task.TaskAlreadyExistsException;
import com.teamrocket.tms.exceptions.task.TaskStatusIsNotValidForAction;
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

        if (foundTask != null) {
            throw new TaskAlreadyExistsException("A task with title " + taskDTO.getTitle() + " already exists.");
        }
    }

    @Override
    public void validateTaskCanBeAssigned(Task task) {
        if(task.isComplete() == true || task.getUser() != null){
            throw new TaskStatusIsNotValidForAction("Task : " + task.getId() + " : " + task.getTitle() + " not available / cannot be assigned");
        }
    }

    @Override
    public void validateUserCanCompleteTaskObjectives(Long userId, Long userIdFromTask) {
        if(userId != userIdFromTask) {
            throw new InvalidUserCompletesTaskObjective("You are not assign to this task and cannot complete its objectives!");
        }
    }
}
