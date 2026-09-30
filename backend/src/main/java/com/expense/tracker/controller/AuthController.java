package com.expense.tracker.controller;

import com.expense.tracker.dto.LoginRequest;
import com.expense.tracker.dto.LoginResponse;
import com.expense.tracker.dto.RegisterRequest;
import com.expense.tracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * AuthController — public endpoints for registration and login.
 *
 * These endpoints are explicitly permitted in SecurityConfig
 * so NO JWT token is required to call them.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Registers a new user. Returns a success message.
     * No authentication required.
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        String message = authService.register(request);
        // Return as a JSON object: { "message": "User registered successfully" }
        return ResponseEntity.ok(Map.of("message", message));
    }

    /**
     * POST /api/auth/login
     * Authenticates user and returns a JWT token.
     * No authentication required.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
