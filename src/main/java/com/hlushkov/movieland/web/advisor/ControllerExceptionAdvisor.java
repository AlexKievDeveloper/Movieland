package com.hlushkov.movieland.web.advisor;

import com.hlushkov.movieland.common.exception.NoUserFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvisor {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataAccessException.class)
    public void badRequest(DataAccessException e) {
        log.error("DataAccessException was thrown ", e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void badRequest(IllegalArgumentException e) {
        log.error("IllegalArgumentException was thrown: ", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoUserFoundException.class)
    public void badRequest(NoUserFoundException e) {
        log.error("NoUserFoundException was thrown, unsuccessful user authentication: ", e);
    }

}
