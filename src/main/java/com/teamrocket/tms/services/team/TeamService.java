package com.teamrocket.tms.services.team;

import com.teamrocket.tms.models.dtos.TeamDTO;

import java.util.List;

public interface TeamService {

    TeamDTO createTeam(TeamDTO teamDTO);

    List<TeamDTO> getAllTeams();

    TeamDTO getTeamById(Long id);
}
