package com.teamrocket.tms.exceptions.team;

public class TeamAlreadyHasTeamLeaderException extends RuntimeException {

    public TeamAlreadyHasTeamLeaderException(String message){
        super(message);
    }
}
