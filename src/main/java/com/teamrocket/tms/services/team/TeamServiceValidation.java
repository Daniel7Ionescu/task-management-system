package com.teamrocket.tms.services.team;

import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.models.entities.Team;

public interface TeamServiceValidation {

    void validateTeamAlreadyExists(TeamDTO teamDTO);

    void validateTeamAlreadyHasTeamLeader(Team team);
}
