package com.expense.tracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtAuthenticationEntryPoint — handles unauthorized access attempts.
 *
 * This is triggered when:
 *   - A request hits a protected endpoint with NO token
 *   - A request has an INVALID or EXPIRED token
 *
 * Without this, Spring Security returns a plain HTML 401 page.
 * This class returns a clean JSON 401 response instead.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Set response as JSON with 401 status
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Build a consistent JSON error body matching our ErrorResponse structure
        Map<String, Object> body = new HashMap<>();
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", "Access denied: " + authException.getMessage());
        body.put("timestamp", LocalDateTime.now().toString());

        // Write the JSON body to the response output stream
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
