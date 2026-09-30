package com.expense.tracker.service;

import com.expense.tracker.dto.LoginRequest;
import com.expense.tracker.dto.LoginResponse;
import com.expense.tracker.dto.RegisterRequest;

/**
 * AuthService — defines the contract for registration and login.
 * The controller depends on this interface, not the implementation.
 */
public interface AuthService {

    // Registers a new user and returns a success message
    String register(RegisterRequest request);

    // Authenticates user credentials and returns a JWT token response
    LoginResponse login(LoginRequest request);
}
