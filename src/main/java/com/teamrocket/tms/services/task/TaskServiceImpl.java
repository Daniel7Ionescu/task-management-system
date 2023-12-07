package com.teamrocket.tms.services.task;

import com.teamrocket.tms.exceptions.task.TaskNotFoundException;
import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final TaskServiceValidation taskServiceValidation;

    public TaskServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper, TaskServiceValidation taskServiceValidation) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
        this.taskServiceValidation = taskServiceValidation;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, User userEntity) {
        taskServiceValidation.validateTaskAlreadyExists(taskDTO);

        Task taskEntity = modelMapper.map(taskDTO, Task.class);
        taskEntity.setCreatedBy(userEntity.getFirstName() + " " + userEntity.getLastName());

        Task savedTaskEntity = taskRepository.save(taskEntity);
        log.info("Task {} : {} inserted in db.", savedTaskEntity.getId(), taskEntity.getTitle());

        return modelMapper.map(savedTaskEntity, TaskDTO.class);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<TaskDTO> taskDTOList = new ArrayList<>();

        taskRepository.findAll().forEach(element -> taskDTOList.add(modelMapper.map(element, TaskDTO.class)));
        log.info("Task list retrieved.");

        return taskDTOList;
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found."));
        log.info("Task with the id {} retrieved.", id);

        return modelMapper.map(task, TaskDTO.class);
    }
}
