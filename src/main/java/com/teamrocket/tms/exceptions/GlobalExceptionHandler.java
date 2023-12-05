package com.teamrocket.tms.exceptions;

import com.teamrocket.tms.exceptions.task.TaskAlreadyExistsException;
import com.teamrocket.tms.exceptions.task.TaskNotFoundException;
import com.teamrocket.tms.exceptions.team.TeamAlreadyExistsException;
import com.teamrocket.tms.exceptions.team.TeamNotFoundException;
import com.teamrocket.tms.exceptions.user.UserAlreadyExistsException;
import com.teamrocket.tms.exceptions.user.UserNotFoundException;
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

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException e) {
        return getResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskAlreadyExistsException.class)
    public ResponseEntity<Object> handleTaskAlreadyExistsException(TaskAlreadyExistsException e) {
        return getResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<Object> handleTeamNotFoundException(TeamNotFoundException e) {
        return getResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<Object> handleTeamAlreadyExistsException(TeamAlreadyExistsException e) {
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

    private ResponseEntity<Object> getResponse(RuntimeException e, HttpStatus httpStatus) {
        Map<String, Object> result = new HashMap<>();
        result.put("message: ", e.getMessage());
        return new ResponseEntity<>(result, httpStatus);
    }
}
