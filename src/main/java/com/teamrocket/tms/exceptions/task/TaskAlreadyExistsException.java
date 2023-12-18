package com.teamrocket.tms.exceptions.task;

public class TaskAlreadyExistsException extends RuntimeException {

    public TaskAlreadyExistsException(String message){
        super(message);
    }
}
