package com.teamrocket.tms.controllers;

import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.services.team.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

//    @PostMapping
//    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamDTO teamDTO){
//        return ResponseEntity.ok(teamService.createTeam(teamDTO));
//    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams(){
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id){
        return ResponseEntity.ok(teamService.getTeamById(id));
    }
}
