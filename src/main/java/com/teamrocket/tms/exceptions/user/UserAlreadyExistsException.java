package com.teamrocket.tms.exceptions.user;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message) { super (message);}
}
