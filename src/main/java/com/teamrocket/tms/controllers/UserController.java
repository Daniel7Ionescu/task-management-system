package com.teamrocket.tms.controllers;

import com.teamrocket.tms.models.dtos.*;
import com.teamrocket.tms.services.user.UserService;
import jakarta.validation.Valid;
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

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userId, userDTO));
    }

    @PutMapping("/{userId}/{targetUserId}")
    public ResponseEntity<UserDTO> updateUserRole(@PathVariable Long userId, @RequestBody UserDTO userDTO, @PathVariable Long targetUserId) {
        return ResponseEntity.ok(userService.updateUserRole(userId, userDTO, targetUserId));
    }

    @PostMapping("/{userId}/tasks")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.createTask(taskDTO, userId));
    }

    @PutMapping("/{userId}/tasks/{taskId}/{targetUserId}")
    public ResponseEntity<UserDTO> assignTask(@PathVariable Long userId, @PathVariable Long taskId, @PathVariable Long targetUserId) {
        return ResponseEntity.ok(userService.assignTask(userId, taskId, targetUserId));
    }

    @GetMapping("{userId}/tasks/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long userId, @PathVariable Long taskId) {
        return ResponseEntity.ok(userService.getTaskById(userId, taskId));
    }

    @PutMapping("{userId}/tasks/{taskId}")
    public ResponseEntity<TaskDTO> completeTaskObjective(@PathVariable Long userId, @PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(userService.userCompleteTaskObjectives(userId, taskId, taskDTO));
    }

    @PutMapping("/{userId}/tasks/filtered")
    public ResponseEntity<List<TaskDTO>> getFilteredTasks(@PathVariable Long userId, @RequestBody TaskFilterDTO parameters) {
        return ResponseEntity.ok(userService.getFilteredTasks(userId, parameters));
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasksForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getAllTasksForUser(userId));
    }

    @PutMapping("/{userId}/tasks/{taskId}/review")
    public ResponseEntity<TaskDTO> reviewTask(@PathVariable Long userId, @PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(userService.reviewTask(userId, taskId, taskDTO));
    }

    @PostMapping("/{userId}/tasks/{taskId}/comments")
    public ResponseEntity<TaskDTO> addCommentToTask(@PathVariable Long userId, @PathVariable Long taskId, @RequestBody String comment) {
        return ResponseEntity.ok(userService.addCommentToTask(userId, taskId, comment));
    }

    @PostMapping("/{userId}/projects")
    public ResponseEntity<ProjectDTO> createProject(@PathVariable Long userId, @Valid @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(userService.createProject(userId, projectDTO));
    }

    @PostMapping("/{userId}/teams")
    public ResponseEntity<TeamDTO> createTeam(@PathVariable Long userId, @Valid @RequestBody TeamDTO teamDTO) {
        return ResponseEntity.ok(userService.createTeam(userId, teamDTO));
    }

    @PostMapping("/{userId}/teams/{teamId}/{targetUserId}")
    public ResponseEntity<TeamDTO> assignTeamLeader(@PathVariable Long userId, @PathVariable Long teamId, @PathVariable Long targetUserId) {
        return ResponseEntity.ok(userService.assignTeamLeader(userId, teamId, targetUserId));
    }

    @PutMapping("/{userId}/teams/{teamId}/{targetProjectId}")
    public ResponseEntity<TeamDTO> assignProjectToTeam(@PathVariable Long userId, @PathVariable Long teamId, @PathVariable Long targetProjectId) {
        return ResponseEntity.ok(userService.assignProjectToTeam(userId, teamId, targetProjectId));
    }

    @PutMapping("/{userId}/teams/{teamId}/assignUser/{targetUserId}")
    public ResponseEntity<UserDTO> assignUserToTeam(@PathVariable Long userId, @PathVariable Long teamId, @PathVariable Long targetUserId) {
        return ResponseEntity.ok(userService.assignUserToTeam(userId, teamId, targetUserId));
    }
}