package com.teamrocket.tms.services;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Task;
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

    public TaskServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task taskEntity = modelMapper.map(taskDTO, Task.class);
        Task savedTaskEntity = taskRepository.save(taskEntity);
        log.info("Task {} : {} inserted in db.", taskEntity.getId(), taskEntity.getTitle());

        return modelMapper.map(savedTaskEntity, TaskDTO.class);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> taskList = taskRepository.findAll();
        List<TaskDTO> taskDTOList = new ArrayList<>();
        taskList.stream()
                .forEach(element -> taskDTOList.add(modelMapper.map(element, TaskDTO.class)));
        log.info("Task list retrieved.");

        return taskDTOList;
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("task.not.found"));
        log.info("Task with the id {} retrieved.", id);

        return modelMapper.map(task, TaskDTO.class);
    }
}
