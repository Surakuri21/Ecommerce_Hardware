package com.Surakuri.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ... (Your existing handleResourceNotFound method is here)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        // ... your existing code ...
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), "NOT_FOUND"), HttpStatus.NOT_FOUND);
    }

    // ... (Your existing handleIllegalArgument method is here)

    // ======================================================
    //  PASTE YOUR NEW METHODS HERE (Before the Generic one)
    // ======================================================

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleUserExists(UserAlreadyExistsException ex, WebRequest request) {
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "CONFLICT" // HTTP 409 is best for duplicates
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<ErrorDetails> handleOutOfStock(ProductOutOfStockException ex, WebRequest request) {
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "BAD_REQUEST"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ======================================================
    //  END OF NEW PASTE
    // ======================================================

    // Keep this one at the very bottom as the "Catch-All"
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception ex, WebRequest request) {
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                "An internal server error occurred.",
                request.getDescription(false),
                "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}