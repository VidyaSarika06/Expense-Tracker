package com.expense.tracker.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response body returned for all API errors.
 * Consistent structure makes it easy for clients to parse errors.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    private int status;                    // HTTP status code e.g. 404
    private String error;                  // Short error label e.g. "Not Found"
    private String message;                // Detailed message
    private LocalDateTime timestamp;       // When the error occurred
    private Map<String, String> fieldErrors; // Populated only for validation errors
}
