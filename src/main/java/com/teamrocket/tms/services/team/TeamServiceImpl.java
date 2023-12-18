package com.teamrocket.tms.services.team;

import com.teamrocket.tms.exceptions.team.TeamNotFoundException;
import com.teamrocket.tms.models.dtos.TeamDTO;
import com.teamrocket.tms.models.entities.Team;
import com.teamrocket.tms.repositories.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final TeamServiceValidation teamServiceValidation;

    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper modelMapper, TeamServiceValidation teamServiceValidation) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.teamServiceValidation = teamServiceValidation;
    }

    @Override
    public TeamDTO createTeam(TeamDTO teamDTO) {
        teamServiceValidation.validateTeamAlreadyExists(teamDTO);
        Team team = modelMapper.map(teamDTO, Team.class);

        Team savedTeam = teamRepository.save(team);
        log.info("Team {} : {} inserted into db", savedTeam.getId(), savedTeam.getName());

        return modelMapper.map(savedTeam, TeamDTO.class);
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        log.info("Retrieved team list");
        return teamRepository.findAll().stream()
                .map(team -> modelMapper.map(team, TeamDTO.class))
                .toList();
    }

    @Override
    public TeamDTO getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team with id: " + id + " not found"));
        log.info("Team with id: {} retrieved", id);

        return modelMapper.map(team, TeamDTO.class);
    }

    @Override
    public TeamDTO assignTeamLeader(Long teamId, Long leaderId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team with id: " + teamId + " not found"));
        log.info("Team with id: {} retrieved", teamId);

        team.setTeamLeaderId(leaderId);
        Team savedTeam = teamRepository.save(team);
        log.info("Team {} : {} set teamLeader to {}", savedTeam.getId(), savedTeam.getName(), leaderId);

        return modelMapper.map(savedTeam, TeamDTO.class);
    }

    @Override
    public void validateTeamAlreadyHasTeamLeader(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team with id: " + teamId + " not found"));

        teamServiceValidation.validateTeamAlreadyHasTeamLeader(team);
    }

    @Override
    public Team updateTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public void validateTeamIsAssignable(TeamDTO teamDTO) {
        teamServiceValidation.validateTeamIsAssignable(teamDTO);
    }
}
