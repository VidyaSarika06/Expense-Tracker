package com.expense.tracker.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler — catches ALL exceptions across the application
 * and returns consistent JSON error responses.
 *
 * Handles:
 *   - Business exceptions  (404, 409)
 *   - Validation errors    (400)
 *   - Security exceptions  (401, 403)
 *   - JWT exceptions       (401)
 *   - Generic fallback     (500)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 404 Not Found ──────────────────────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null);
    }

    // ── 409 Conflict (duplicate email, duplicate category name) ───────────────
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), null);
    }

    // ── 400 Validation Errors (@Valid failures) ────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed",
                "One or more fields are invalid", fieldErrors);
    }

    // ── 401 Bad Credentials (wrong email or password on login) ────────────────
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized",
                "Invalid email or password", null);
    }

    // ── 401 Disabled Account ──────────────────────────────────────────────────
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabled(DisabledException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized",
                "Account is disabled", null);
    }

    // ── 401 General Authentication Failure ────────────────────────────────────
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), null);
    }

    // ── 401 Expired JWT Token ─────────────────────────────────────────────────
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized",
                "JWT token has expired. Please login again.", null);
    }

    // ── 401 Invalid JWT Token (tampered, malformed, wrong signature) ──────────
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJwt(JwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized",
                "Invalid JWT token", null);
    }

    // ── 403 Access Denied (authenticated but wrong role) ─────────────────────
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden",
                "You do not have permission to access this resource", null);
    }

    // ── 500 Internal Server Error (catch-all) ─────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                ex.getMessage(), null);
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error,
                                                         String message,
                                                         Map<String, String> fieldErrors) {
        ErrorResponse body = new ErrorResponse(
                status.value(), error, message, LocalDateTime.now(), fieldErrors);
        return new ResponseEntity<>(body, status);
    }
}
