package com.teamrocket.tms.controllers;


import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userId, userDTO));
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<TaskDTO> createTask( @Valid @RequestBody TaskDTO taskDTO, @PathVariable long id) {
        return ResponseEntity.ok(userService.createTask(taskDTO, id));
    }

    @GetMapping("{userId}/tasks/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long userId, @PathVariable Long taskId){
        return ResponseEntity.ok(userService.getTaskById(userId, taskId));
    }

    @PostMapping("/{userId}/projects")
    public ResponseEntity<ProjectDTO> createProject(@PathVariable Long userId, @Valid @RequestBody ProjectDTO projectDTO){
        return ResponseEntity.ok(userService.createProject(userId, projectDTO));
    }

    @PostMapping("/{userId}/teams")
    public ResponseEntity<TeamDTO> createTeam(@PathVariable Long userId, @Valid @RequestBody TeamDTO teamDTO) {
        return ResponseEntity.ok(userService.createTeam(userId, teamDTO));
    }
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasksForUser(@PathVariable Long userId) {
        List<TaskDTO> tasks = userService.getAllTasksForUser(userId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}