package com.teamrocket.tms.exceptions.user;

public class UserNotInATeamException extends RuntimeException {

    public UserNotInATeamException(String message) {
        super(message);
    }
}
