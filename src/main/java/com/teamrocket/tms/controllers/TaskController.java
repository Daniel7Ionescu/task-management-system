package com.teamrocket.tms.controllers;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.services.task.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping("/{id}/abandon")
    public ResponseEntity<String> abandonTask(@PathVariable Long id,@RequestParam Long userId) {
        taskService.abandonTask(id,userId);
        return new ResponseEntity<>("Task abandoned successufully.", HttpStatus.OK);
    }
}