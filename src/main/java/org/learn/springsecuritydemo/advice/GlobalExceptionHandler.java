package org.learn.springsecuritydemo.advice;

import io.jsonwebtoken.JwtException;
import org.learn.springsecuritydemo.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception){
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(error,error.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException exception){
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException exception){
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException exception){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
