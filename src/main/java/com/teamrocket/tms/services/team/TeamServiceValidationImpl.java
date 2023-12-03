package com.teamrocket.tms.services.team;

import com.teamrocket.tms.exceptions.team.TeamAlreadyExistsException;
import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.models.entities.Team;
import com.teamrocket.tms.repositories.TeamRepository;
import org.springframework.stereotype.Component;

@Component
public class TeamServiceValidationImpl implements TeamServiceValidation {

    private final TeamRepository teamRepository;

    public TeamServiceValidationImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void validateTeamAlreadyExists(TeamDTO teamDTO) {
        Team foundTeam = teamRepository.findByName(teamDTO.getName());

        if (foundTeam != null) {
            throw new TeamAlreadyExistsException("A team with name: " + teamDTO.getName() + " already exists");
        }
    }
}
