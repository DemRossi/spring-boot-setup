package com.exercise.springbootsetup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException e){
        // Create payload containing exception details
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ServiceExceptionPayload serviceExceptionPayload = new ServiceExceptionPayload(
                badRequest,
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        // return response entity
        return new ResponseEntity<>(serviceExceptionPayload, badRequest);
    }
}
