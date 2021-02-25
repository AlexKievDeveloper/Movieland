package com.hlushkov.movieland.web.advisor;

import com.hlushkov.movieland.common.exception.NoUserFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvisor {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> badRequest(DataAccessException e) {
        log.info("DataAccessException was thrown ", e);
        String message = "Please check your input data and try again.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException e) {
        log.error("IllegalArgumentException was thrown: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<Map<String, String>> badRequest(NoUserFoundException e) {
        log.error("NoUserFoundException was thrown, unsuccessful user authentication: " + e);
        String message = "Invalid email or password. Please check your credentials and try again.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", message));
    }

}
