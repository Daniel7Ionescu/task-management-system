package com.teamrocket.tms.services.team;

import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.models.entities.User;

import java.util.List;

public interface TeamService {

    TeamDTO createTeam(TeamDTO teamDTO);

    List<TeamDTO> getAllTeams();

    TeamDTO getTeamById(Long id);

    TeamDTO assignTeamLeader(Long teamId, Long leaderId);
}
