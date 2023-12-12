package com.teamrocket.tms.controllers;

import com.teamrocket.tms.models.dtos.TaskDTO;
import com.teamrocket.tms.models.dtos.ProjectDTO;
import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.models.dtos.UserDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userId, userDTO));
    }

    @PostMapping("/{userId}/tasks")
    public ResponseEntity<TaskDTO> createTask( @Valid @RequestBody TaskDTO taskDTO, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.createTask(taskDTO, userId));
    }

    @PutMapping("/{userId}/tasks/{taskId}/{targetUserId}")
    public ResponseEntity<UserDTO> assignTask(@PathVariable Long userId, @PathVariable Long taskId, @PathVariable Long targetUserId){
        return ResponseEntity.ok(userService.assignTask(userId, taskId, targetUserId));
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

    @PostMapping("/{userId}/teams/{teamId}/{leaderId}")
    public ResponseEntity<TeamDTO> assignTeamLeader(@PathVariable Long userId, @PathVariable Long teamId, @PathVariable Long leaderId) {
        return ResponseEntity.ok(userService.assignTeamLeader(userId, teamId, leaderId));
    }

    @PutMapping("/{userId}/teams/{teamId}/{targetProjectId}")
    public ResponseEntity<TeamDTO> assignProjectToTeam(@PathVariable Long userId, @PathVariable Long teamId, @PathVariable Long targetProjectId) {
        return ResponseEntity.ok(userService.assignProjectToTeam(userId, teamId, targetProjectId));
    }
}
