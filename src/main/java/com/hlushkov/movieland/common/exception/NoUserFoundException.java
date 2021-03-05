package com.hlushkov.movieland.common.exception;

public class NoUserFoundException extends RuntimeException {

    public NoUserFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
