package com.teamrocket.tms.services.team;

import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.models.entities.Team;

import java.util.List;

public interface TeamService {

    TeamDTO createTeam(TeamDTO teamDTO);

    List<TeamDTO> getAllTeams();

    TeamDTO getTeamById(Long id);

    Team updateTeam(Team team);

    void validateTeamIsAssignable(TeamDTO teamDTO);
}
