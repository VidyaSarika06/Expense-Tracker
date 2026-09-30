package com.expense.tracker.service.impl;

import com.expense.tracker.dto.LoginRequest;
import com.expense.tracker.dto.LoginResponse;
import com.expense.tracker.dto.RegisterRequest;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.DuplicateResourceException;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.security.JwtUtil;
import com.expense.tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthServiceImpl — implements registration and login business logic.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository      userRepository;
    private final PasswordEncoder     passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil             jwtUtil;
    private final UserDetailsService  userDetailsService;

    // Allowed roles — single source of truth for validation
    private static final String ROLE_STUDENT    = "ROLE_STUDENT";
    private static final String ROLE_INDIVIDUAL = "ROLE_INDIVIDUAL";

    /**
     * Register a new user.
     *
     * Steps:
     *   1. Check if email is already registered
     *   2. Resolve the role:
     *        - null / blank  → default to ROLE_STUDENT
     *        - valid value   → use as-is
     *        (invalid values are already rejected by @Pattern in RegisterRequest)
     *   3. Hash the password with BCrypt
     *   4. Save the user with the resolved role
     */
    @Override
    @Transactional
    public String register(RegisterRequest request) {

        // Step 1: Reject duplicate emails
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email is already registered: " + request.getEmail());
        }

        // Step 2: Resolve role — default to ROLE_STUDENT when not provided
        // Note: @Pattern on RegisterRequest already blocks any value that is
        // not ROLE_STUDENT or ROLE_INDIVIDUAL, so no further validation needed here.
        String role = (request.getRole() == null || request.getRole().isBlank())
                ? ROLE_STUDENT
                : request.getRole();

        // Step 3 & 4: Build and save the user with the resolved role
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    /**
     * Login an existing user.
     *
     * Steps:
     *   1. Authenticate credentials via AuthenticationManager
     *   2. Load UserDetails from DB
     *   3. Generate JWT token
     *   4. Return token + email + role in response
     */
    @Override
    public LoginResponse login(LoginRequest request) {

        // Step 1: Authenticate — throws BadCredentialsException on failure
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        // Step 2: Load UserDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Step 3: Generate JWT
        String token = jwtUtil.generateToken(userDetails);

        // Step 4: Fetch user entity to return the actual stored role
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole()) // returns "ROLE_STUDENT" or "ROLE_INDIVIDUAL"
                .build();
    }
}
