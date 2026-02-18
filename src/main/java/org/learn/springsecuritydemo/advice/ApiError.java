package org.learn.springsecuritydemo.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ApiError {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;

    public ApiError(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message){
        this();
        this.status = status;
        this.message = message;
    }

}
