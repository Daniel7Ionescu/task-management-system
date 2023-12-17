package com.teamrocket.tms.controllers;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.services.task.TaskService;
//import jakarta.validation.Valid;
import com.teamrocket.tms.utils.enums.Priority;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

//    @PostMapping
//    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
//        return ResponseEntity.ok(null);
//    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

//    @GetMapping("/filtered")
//    public ResponseEntity<List<TaskDTO>> getFilteredTasks(@RequestParam Map<String, String> parameters) {
//        return ResponseEntity.ok(taskService.getFilteredTasks(parameters));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }
}
