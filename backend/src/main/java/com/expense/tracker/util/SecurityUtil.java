package com.expense.tracker.util;

import com.expense.tracker.entity.User;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * SecurityUtil — resolves the currently authenticated User from the SecurityContext.
 *
 * Flow:
 *   JWT filter sets the principal (email) in SecurityContextHolder.
 *   This helper reads that email and loads the full User entity from the DB.
 *
 * Used by all services that need to scope data to the logged-in user.
 * Frontend never sends userId — the backend always derives it from the JWT.
 */
@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;

    /**
     * Returns the User entity for the currently authenticated principal.
     * Throws ResourceNotFoundException (404) if the email is not found in the DB.
     */
    public User getLoggedInUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // principal name = email (set by JwtAuthenticationFilter)

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + email));
    }
}
