package com.manjappa.store.exceptions;

import com.manjappa.store.dtos.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnreadableMessage() {
        return ResponseEntity.badRequest().body(
                //Map.of("error", "Invalid request body")
                new ErrorDto("Invalid request body")
        );
    }

    //MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException myException) {
        Map<String, String> errors = new HashMap<>();
        myException.getBindingResult().getFieldErrors().forEach((error1) -> {
            errors.put(error1.getField(), error1.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
