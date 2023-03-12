package com.server.dos.exception;

import com.server.dos.exception.custom.OrderException;
import com.server.dos.exception.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleOrderException(OrderException ex) {
        log.error("Order Exception", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode().getCode(), ex.getErrorMessage());
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleMeetingException(OrderException ex) {
        log.error("Meeting Exception", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode().getCode(), ex.getErrorMessage());
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }
}
