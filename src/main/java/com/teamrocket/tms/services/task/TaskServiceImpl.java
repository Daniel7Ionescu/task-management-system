package com.teamrocket.tms.services.task;

import com.teamrocket.tms.exceptions.task.TaskNotFoundException;
import com.teamrocket.tms.exceptions.task.TaskStatusIsNotValidForAction;
import com.teamrocket.tms.exceptions.user.UsersAreEqualsException;
import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.repositories.TaskRepository;
import com.teamrocket.tms.utils.enums.Status;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.teamrocket.tms.utils.calculators.CompletionCalculator.*;

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
    public List<TaskDTO> getFilteredTasks(Map<String, String> parameters, Project project) {
        List<List<TaskDTO>> resultList = new ArrayList<>();

        for (String key : parameters.keySet()) {
            if (key.equals("userId")) {
                resultList.add(taskRepository.findByUserId(Long.valueOf(parameters.get(key))).stream()
                        .filter(element -> element.getProject().equals(project))
                        .map(element -> modelMapper.map(element, TaskDTO.class))
                        .toList());
            }
            if (key.equals("objectives")) {
                resultList.add(taskRepository.findByProject(project).stream()
                        .filter(element -> element.getProject().equals(project))
                        .filter(element -> element.getObjectives().size() == Integer.parseInt(parameters.get(key)))
                        .map(element -> modelMapper.map(element, TaskDTO.class))
                        .toList());
            }
            if (key.equals("dueDate")) {
                resultList.add(taskRepository.findByDueDate(LocalDate.parse(parameters.get(key))).stream()
                        .filter(element -> element.getProject().equals(project))
                        .map(element -> modelMapper.map(element, TaskDTO.class))
                        .toList());
            }
            if (key.equals("priority")) {
                resultList.add(taskRepository.findByProject(project).stream()
                        .filter(element -> element.getPriority() != null)
                        .filter(element -> element.getPriority().getPriorityLabel().equals(parameters.get(key)))
                        .map(element -> modelMapper.map(element, TaskDTO.class))
                        .toList());
            }
        }

        List<TaskDTO> result = new ArrayList<>(resultList.get(0));
        for (List<TaskDTO> list : resultList) {
            result.retainAll(list);
        }

        return result;
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
        task.setProgress(getPercentageComplete(task.getObjectives()));
        task.setComplete(checkCompleteBasedOnProgress(task.getProgress()));
        task.setStatus(setTaskStatusBasedOnProgress((int) task.getProgress()));

        if (task.isComplete()) {
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

        //update project percentage complete

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
}