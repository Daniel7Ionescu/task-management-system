package com.teamrocket.tms.exceptions.task;

public class TaskStatusIsNotValidForAction extends RuntimeException {

    public TaskStatusIsNotValidForAction(String message){
        super(message);
    }
}
