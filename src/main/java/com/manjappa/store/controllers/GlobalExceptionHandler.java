package com.manjappa.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
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
