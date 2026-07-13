package com.ogos.exception;

import com.ogos.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of("Invalid email or password"));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(java.nio.file.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(java.nio.file.AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of("Access Denied: " + ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleSpringAccessDenied(
            org.springframework.security.access.AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of("Access Denied: You do not have permission to perform this action"));
    }

    // ── Duplicate DB entries (unique constraint violations) → 409 ──────────
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException ex) {

        String message = "Duplicate entry: a record with this value already exists";
        String cause = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

        if (cause.contains("duplicate entry")) {
            if (cause.contains("email")) {
                message = "Email already registered";
            } else if (cause.contains("categories")) {
                message = "A category with this name already exists";
            } else if (cause.contains("product_name") || cause.contains("products")) {
                message = "A product with this name already exists";
            } else if (cause.contains("users")) {
                message = "A user with this value already exists";
            }
        } else if (cause.contains("cannot delete") || cause.contains("foreign key")) {
            message = "Cannot delete: this record is used by other data (e.g. products in this category)";
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of("Validation Failed", errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(io.jsonwebtoken.ExpiredJwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of("JWT token has expired. Please login again."));
    }

    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(io.jsonwebtoken.JwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of("Invalid JWT token"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("Internal Server Error: " + ex.getMessage()));
    }
}
