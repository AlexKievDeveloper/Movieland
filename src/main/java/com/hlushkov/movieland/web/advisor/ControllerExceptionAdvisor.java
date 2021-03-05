package com.hlushkov.movieland.web.advisor;

import com.hlushkov.movieland.common.exception.NoUserFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvisor {
    private final String constantMessage = "message";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataAccessException.class)
    public void badRequest(DataAccessException e) {
        log.error("DataAccessException was thrown ", e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException e) {
        log.error("IllegalArgumentException was thrown: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(constantMessage, e.getMessage()));
    }

    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<Map<String, String>> badRequest(NoUserFoundException e) {
        log.error("NoUserFoundException was thrown, unsuccessful user authentication: " + e);
        String message = "Invalid email or password. Please check your credentials and try again.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(constantMessage, message));
    }

}
