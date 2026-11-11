package com.example.questionbank.api;

import com.example.questionbank.application.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

/** Converts application and validation failures into explicit HTTP error responses. */
@RestControllerAdvice
public class ApiExceptionHandler {
    /** Returns 404 for missing resources. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(ResourceNotFoundException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage());
    }
    /** Returns 400 for invalid input and role assignments. */
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> invalidRequest(Exception exception) {
        String message = exception instanceof MethodArgumentNotValidException validation
                ? validation.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage()).findFirst().orElse("Invalid request")
                : exception.getMessage();
        return response(HttpStatus.BAD_REQUEST, message);
    }
    /** Returns 409 when a business workflow transition is not allowed. */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflict(IllegalStateException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage());
    }
    /** Returns 409 when a database uniqueness or foreign-key constraint is violated. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> dataConflict(DataIntegrityViolationException exception) {
        return response(HttpStatus.CONFLICT, "The request conflicts with an existing record or relationship");
    }
    private ResponseEntity<Map<String, String>> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message == null ? status.getReasonPhrase() : message));
    }
}
