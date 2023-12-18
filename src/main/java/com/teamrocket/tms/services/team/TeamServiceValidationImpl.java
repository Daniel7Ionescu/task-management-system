package com.teamrocket.tms.services.team;

import com.teamrocket.tms.exceptions.team.TeamAlreadyExistsException;
import com.teamrocket.tms.exceptions.team.TeamAlreadyHasTeamLeaderException;
import com.teamrocket.tms.exceptions.team.TeamIsNotAssignableException;
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

    @Override
    public void validateTeamAlreadyHasTeamLeader(Team team) {
        if (team.getTeamLeaderId() != null) {
            throw new TeamAlreadyHasTeamLeaderException("Team with name: " + team.getName() + " already has a team leader.");
        }
    }

    @Override
    public void validateTeamIsAssignable(TeamDTO teamDTO) {
        if (teamDTO.getProject() != null) {
            throw new TeamIsNotAssignableException("Team: " + teamDTO.getId() + " : " + teamDTO.getName() + " not available / cannot be assigned.");
        }
    }
}
