package com.edee.foundationsforfaith.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class UnableToInsertException extends RuntimeException {
    private final HttpStatus errorCode;
    private final LocalDateTime timestamp;

    public UnableToInsertException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
