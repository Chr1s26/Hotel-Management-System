package com.project.HotelManagementSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNameNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "USER_NOT_FOUND");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex){

        Map<String,Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status",404);
        body.put("error","RESOURCE_NOT_FOUND");
        body.put("message",ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(OtpInvalidException.class)
    public ResponseEntity<?> handleOtpException(OtpInvalidException ex){

        Map<String,Object> body = new LinkedHashMap<>();

        body.put("status",400);
        body.put("error","INVALID_OTP");
        body.put("message",ex.getMessage());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<?> handleOtpException(OtpExpiredException ex){

        Map<String,Object> body = new LinkedHashMap<>();

        body.put("status",400);
        body.put("error","OTP_EXPIRED");
        body.put("message",ex.getMessage());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex){

        Map<String,Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status",500);
        body.put("error","INTERNAL_SERVER_ERROR");
        body.put("message",ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
