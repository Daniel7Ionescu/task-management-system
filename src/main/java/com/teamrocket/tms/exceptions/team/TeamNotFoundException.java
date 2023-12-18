package com.teamrocket.tms.exceptions.team;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(String message){
        super(message);
    }
}
