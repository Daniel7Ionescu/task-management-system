package com.teamrocket.tms.exceptions;

import com.teamrocket.tms.models.dtos.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorDTO errorDTO = new ErrorDTO();

        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errorDTO.getMessage().put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
