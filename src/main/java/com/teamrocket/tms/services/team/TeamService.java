package com.teamrocket.tms.services.team;

import com.teamrocket.tms.models.dtos.TeamDTO;
<<<<<<< HEAD
import com.teamrocket.tms.models.entities.User;
=======
import com.teamrocket.tms.models.entities.Team;
>>>>>>> dev

import java.util.List;

public interface TeamService {

    TeamDTO createTeam(TeamDTO teamDTO);

    List<TeamDTO> getAllTeams();

    TeamDTO getTeamById(Long id);

<<<<<<< HEAD
    TeamDTO assignTeamLeader(Long teamId, Long leaderId);
=======
    Team updateTeam(Team team);

    void validateTeamIsAssignable(TeamDTO teamDTO);
>>>>>>> dev
}
