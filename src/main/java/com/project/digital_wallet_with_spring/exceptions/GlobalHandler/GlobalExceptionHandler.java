package com.project.digital_wallet_with_spring.exceptions.GlobalHandler;

import com.project.digital_wallet_with_spring.dtos.exception.ErrorResponse;
import com.project.digital_wallet_with_spring.exceptions.user.EmailAlreadyExist;
import com.project.digital_wallet_with_spring.exceptions.user.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Validation Failed")
                        .message("Invalid request fields")
                        .fieldErrors(fieldErrors)
                        .build()
        );
    }

    @ExceptionHandler(EmailAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExist(EmailAlreadyExist ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.CONFLICT.value())
                        .error("Conflict")
                        .message(ex.getMessage())
                        .build()
        );

    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFound ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.CONFLICT.value())
                        .error("Conflict")
                        .message(ex.getMessage())
                        .build()
        );
    }



}
