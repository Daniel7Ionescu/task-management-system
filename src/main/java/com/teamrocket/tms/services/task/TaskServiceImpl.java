package com.teamrocket.tms.services.task;

import com.teamrocket.tms.exceptions.task.TaskNotFoundException;
import com.teamrocket.tms.exceptions.task.TaskStatusIsNotValidForAction;
import com.teamrocket.tms.exceptions.user.UsersAreEqualsException;
import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.dtos.TaskFilterDTO;
import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.repositories.TaskRepository;
import com.teamrocket.tms.services.project.ProjectService;
import com.teamrocket.tms.utils.enums.Status;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.teamrocket.tms.utils.calculators.CompletionCalculator.*;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final TaskServiceValidation taskServiceValidation;
    private final ProjectService projectService;

    public TaskServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper, TaskServiceValidation taskServiceValidation, ProjectService projectService) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
        this.taskServiceValidation = taskServiceValidation;
        this.projectService = projectService;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, UserDTO userDTO, Project project) {
        taskServiceValidation.validateTaskAlreadyExists(taskDTO);

        taskDTO.setCreatedBy(userDTO.getFirstName() + " " + userDTO.getLastName());
        taskDTO.setProject(userDTO.getTeam().getProject());
        taskDTO.setStatus(Status.TO_DO);

        Task taskEntity = modelMapper.map(taskDTO, Task.class);
        taskEntity.setCreatedBy(userDTO.getFirstName() + " " + userDTO.getLastName());
        taskEntity.setProject(project);

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
    public List<TaskDTO> getFilteredTasks(TaskFilterDTO parameters, Project project) {
        if (isFilterTaskDTONull(parameters))
        {
            return taskRepository.findAll().stream()
                    .map(element -> modelMapper.map(element, TaskDTO.class))
                    .toList();
        }

        return taskRepository.findFilteredTasks(parameters.getId(), parameters.getDueDate(), parameters.getPriority()).stream()
                .filter(element -> Integer.valueOf(element.getObjectives().size()).equals(parameters.getObjectives()))
                .map(element -> modelMapper.map(element, TaskDTO.class))
                .toList();
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found."));
        log.info("Task with the id {} retrieved.", id);

        return modelMapper.map(task, TaskDTO.class);
    }

    @Override
    public TaskDTO completeTaskObjectives(Long userId, Long taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found."));
        log.info("Task with the id {} retrieved.", taskId);

        taskServiceValidation.validateUserCanCompleteTaskObjectives(userId, task.getUser().getId());

        task.setObjectives(taskDTO.getObjectives());
        task.setProgress(getTaskPercentageComplete(task.getObjectives()));
        task.setObjectiveMapComplete(checkCompleteBasedOnProgress(task.getProgress()));
        task.setStatus(setTaskStatusBasedOnProgress((int) task.getProgress()));

        if (task.isObjectiveMapComplete()) {
            task.setCompletedBy(task.getUser().getFirstName() + " " + task.getUser().getLastName());
        } else {
            task.setCompletedBy(null);
        }
        Task savedTask = taskRepository.save(task);

        return modelMapper.map(savedTask, TaskDTO.class);
    }

    @Override
    public List<TaskDTO> getAllTasksForUser(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .toList();
    }

    @Override
    public TaskDTO userReviewTask(String reviewerName, Long taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found."));
        log.info("Task with the id {} retrieved.", taskId);

        if (!task.getStatus().equals(Status.AWAITING_REVIEW)) {
            throw new TaskStatusIsNotValidForAction("Task cannot be reviewed at this time.");
        }

        if (reviewerName.equals(task.getCompletedBy())) {
            throw new UsersAreEqualsException("You cannot review your own work!");
        }

        task.setStatus(taskDTO.getStatus());
        task.setReviewedBy(reviewerName);
        Task savedTask = taskRepository.save(task);
        projectService.updateProjectPercentageComplete(savedTask.getProject());

        return modelMapper.map(savedTask, TaskDTO.class);
    }

    @Transactional
    @Override
    public TaskDTO addCommentToTask(String userName, Long taskId, String comment) {
        Task task = taskServiceValidation.getValidTask(taskId, "addCommentToTask");

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        task.getComments().put(localDateTime.format(dateTimeFormatter), userName + " : " + comment);
        Task savedTask = taskRepository.save(task);

        return modelMapper.map(savedTask, TaskDTO.class);
    }

    @Override
    public void validateTaskCanBeAssigned(Task task) {
        taskServiceValidation.validateTaskCanBeAssigned(task);
    }

    @Override
    public TaskDTO assignUserToTask(User userEntity, Long taskId) {
        Task task = taskServiceValidation.getValidTask(taskId, "assignUserToTask");

        taskServiceValidation.validateTaskCanBeAssigned(task);

        task.setUser(userEntity);
        Task savedTask = taskRepository.save(task);

        return modelMapper.map(savedTask, TaskDTO.class);
    }

    private Status setTaskStatusBasedOnProgress(int value) {
        switch (value) {
            case 0:
                return Status.TO_DO;
            case 100:
                return Status.AWAITING_REVIEW;
            default:
                return Status.IN_PROGRESS;
        }
    }

    private boolean isFilterTaskDTONull(TaskFilterDTO parameters) {
        return parameters.getId() == null && parameters.getObjectives() == null && parameters.getDueDate() == null && parameters.getPriority() == null;
    }
}