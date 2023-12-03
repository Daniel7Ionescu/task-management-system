package com.teamrocket.tms.services.team;

import com.teamrocket.tms.models.dtos.TeamDTO;

public interface TeamServiceValidation {

    void validateTeamAlreadyExists(TeamDTO teamDTO);
}
