package com.exercise.springbootsetup.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ServiceExceptionPayload {
    private final int httpStatusCode;
    private final HttpStatus httpStatus;
    private final String message;
    private final ZonedDateTime timestamp;

    public ServiceExceptionPayload(HttpStatus httpStatus, String message,  ZonedDateTime timestamp) {
        this.httpStatusCode = httpStatus.value();
        this.httpStatus = httpStatus;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
