package com.teamrocket.tms.services;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.repositories.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public TaskServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task taskEntity = modelMapper.map(taskDTO, Task.class);

        Task savedTaskEntity = taskRepository.save(taskEntity);

        return modelMapper.map(savedTaskEntity, TaskDTO.class);
    }
}
