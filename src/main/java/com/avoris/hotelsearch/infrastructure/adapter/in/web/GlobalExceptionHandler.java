package com.avoris.hotelsearch.infrastructure.adapter.in.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.avoris.hotelsearch.domain.exception.SearchNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(final MethodArgumentNotValidException ex) {
        LOGGER.warn("Validation failed: {}", ex.getMessage());
        final Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed");
        body.put("details", ex.getBindingResult().getFieldErrors().stream().map(f -> f.getField() + " " + f.getDefaultMessage()).toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleNotReadable(final HttpMessageNotReadableException ex) {
        LOGGER.warn("Request body is not readable: {}", ex.getMessage());
        final Map<String, Object> body = new HashMap<>();
        body.put("error", "Malformed JSON or invalid field format");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(SearchNotFoundException.class)
    public ResponseEntity<?> handleNotFound(final SearchNotFoundException ex) {
        LOGGER.warn("Not found: {}", ex.getMessage());
        final Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(final Exception ex) {
        LOGGER.error("Unhandled error", ex);
        final Map<String, Object> body = new HashMap<>();
        body.put("error", "Internal error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
