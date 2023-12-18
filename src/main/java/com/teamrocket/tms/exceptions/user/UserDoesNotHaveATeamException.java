package com.teamrocket.tms.exceptions.user;

public class UserDoesNotHaveATeamException extends RuntimeException {

    public UserDoesNotHaveATeamException(String message) {
        super(message);
    }
}