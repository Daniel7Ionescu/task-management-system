package com.teamrocket.tms.exceptions;

import com.teamrocket.tms.exceptions.project.ProjectAlreadyExistsException;
import com.teamrocket.tms.exceptions.project.ProjectIsNotAssignableException;
import com.teamrocket.tms.exceptions.project.ProjectNotFoundException;
import com.teamrocket.tms.exceptions.task.InvalidUserCompletesTaskObjective;
import com.teamrocket.tms.exceptions.task.TaskAlreadyExistsException;
import com.teamrocket.tms.exceptions.task.TaskStatusIsNotValidForAction;
import com.teamrocket.tms.exceptions.task.TaskNotFoundException;
import com.teamrocket.tms.exceptions.user.*;
import com.teamrocket.tms.exceptions.team.TeamAlreadyExistsException;
import com.teamrocket.tms.exceptions.team.TeamAlreadyHasTeamLeaderException;
import com.teamrocket.tms.exceptions.team.TeamIsNotAssignableException;
import com.teamrocket.tms.exceptions.team.TeamNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, Object> result = new HashMap<>();

        exception.getBindingResult().getFieldErrors()
                .forEach(error -> result.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException e) {
        return getResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProjectAlreadyExistsException.class)
    public ResponseEntity<Object> handleProjectAlreadyExistsException(ProjectAlreadyExistsException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProjectIsNotAssignableException.class)
    public ResponseEntity<Object> handleProjectIsNotAssignableException(ProjectIsNotAssignableException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException e) {
        return getResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskAlreadyExistsException.class)
    public ResponseEntity<Object> handleTaskAlreadyExistsException(TaskAlreadyExistsException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskStatusIsNotValidForAction.class)
    public ResponseEntity<Object> handleTaskIsNotAssignableException(TaskStatusIsNotValidForAction e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidUserCompletesTaskObjective.class)
    public ResponseEntity<Object> handleInvalidUserCompletesTaskObjective(InvalidUserCompletesTaskObjective e) {
        return getResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<Object> handleTeamNotFoundException(TeamNotFoundException e) {
        return getResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<Object> handleTeamAlreadyExistsException(TeamAlreadyExistsException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TeamAlreadyHasTeamLeaderException.class)
    public ResponseEntity<Object> handleTeamAlreadyHasTeamLeader(TeamAlreadyHasTeamLeaderException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TeamIsNotAssignableException.class)
    public ResponseEntity<Object> handleTeamIsNotAssignableException(TeamIsNotAssignableException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsersAreEqualsException.class)
    public ResponseEntity<Object> handlePMCannotBeTM(UsersAreEqualsException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        return getResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyInATeamException.class)
    public ResponseEntity<Object> handleUserAlreadyInATeamException(UserAlreadyInATeamException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotInATeamException.class)
    public ResponseEntity<Object> handleUserNotInATeamException(UserNotInATeamException e) {
        return getResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserUnauthorizedActionException.class)
    public ResponseEntity<Object> handleUserUnauthorizedActionException(UserUnauthorizedActionException e) {
        return getResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDoesNotHaveATeamException.class)
    public ResponseEntity<Object> handleUserDoesNotHaveATeamException(UserDoesNotHaveATeamException e) {
        return getResponse(e, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<Object> getResponse(RuntimeException e, HttpStatus httpStatus) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", e.getMessage());
        return new ResponseEntity<>(result, httpStatus);
    }
}
