package com.framework.galaxion.infra.adapter.in.rest.handler;

import com.framework.galaxion.domain.exception.InvalidOptionForSubscriptionTypeException;
import com.framework.galaxion.domain.exception.SubscriptionNotFoundException;
import com.framework.galaxion.infra.adapter.in.rest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSubscriptionNotFound(
            SubscriptionNotFoundException ex
    ) {
        log.error("Subscription not found: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .error("SUBSCRIPTION_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidOptionForSubscriptionTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOption(
            InvalidOptionForSubscriptionTypeException ex
    ) {
        log.error("Invalid option for subscription type: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .error("INVALID_OPTION")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("Validation error: {}", message);

        ErrorResponse response = ErrorResponse.builder()
                .error("VALIDATION_ERROR")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

