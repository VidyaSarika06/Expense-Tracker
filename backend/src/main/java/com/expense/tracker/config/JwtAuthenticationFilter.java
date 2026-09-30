package com.expense.tracker.config;

import com.expense.tracker.security.CustomUserDetailsService;
import com.expense.tracker.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter — runs ONCE per HTTP request (OncePerRequestFilter).
 *
 * What it does step by step:
 *   1. Read the "Authorization" header
 *   2. Check it starts with "Bearer "
 *   3. Extract the JWT token string
 *   4. Extract the email (username) from the token
 *   5. Load the UserDetails from the database
 *   6. Validate the token
 *   7. Set the Authentication object in the SecurityContext
 *   8. Pass the request to the next filter in the chain
 *
 * If any step fails (invalid/expired token), the request is NOT authenticated
 * and the JwtAuthenticationEntryPoint returns a 401 response.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Read the Authorization header
        final String authHeader = request.getHeader("Authorization");

        // Step 2: If header is missing or doesn't start with "Bearer", skip this filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // continue without authentication
            return;
        }

        // Step 3: Extract the token (remove "Bearer " prefix — 7 characters)
        final String token = authHeader.substring(7);
        String email = null;

        try {
            // Step 4: Extract email from token
            email = jwtUtil.extractUsername(token);
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
            // Don't set authentication — SecurityContext stays empty → 401
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        }

        // Step 5: If email extracted AND no authentication set yet in this request
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user from DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Step 6: Validate token against the loaded user
            if (jwtUtil.validateToken(token, userDetails)) {

                // Step 7: Create authentication token and set it in SecurityContext
                // This tells Spring Security: "this request is authenticated"
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,                          // credentials null (already verified)
                                userDetails.getAuthorities()   // ROLE_USER / ROLE_ADMIN
                        );

                // Attach request details (IP, session) to the authentication object
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in the SecurityContext for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 8: Continue to the next filter / controller
        filterChain.doFilter(request, response);
    }
}
