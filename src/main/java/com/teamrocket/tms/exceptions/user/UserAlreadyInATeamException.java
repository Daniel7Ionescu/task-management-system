package com.teamrocket.tms.exceptions.user;

public class UserAlreadyInATeamException extends RuntimeException {

    public UserAlreadyInATeamException(String message) {
        super(message);
    }
}