package com.expense.tracker.security;

import com.expense.tracker.entity.User;
import com.expense.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CustomUserDetailsService — loads a user from the DB for Spring Security.
 *
 * ROLE STORAGE CONVENTION:
 *   The 'role' column in the 'users' table stores the full Spring Security
 *   authority string including the prefix:
 *
 *     ROLE_STUDENT    → stored as "ROLE_STUDENT"
 *     ROLE_INDIVIDUAL → stored as "ROLE_INDIVIDUAL"
 *
 *   Therefore we use the stored value DIRECTLY as the GrantedAuthority.
 *   We must NOT prepend "ROLE_" again — that would produce "ROLE_ROLE_STUDENT"
 *   which Spring Security would never match against hasRole("STUDENT").
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email));

        // The role column already contains the full authority string e.g. "ROLE_STUDENT"
        // Use it directly — do NOT prepend "ROLE_" again
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),//setting as username
                user.getPassword(),
                List.of(authority)
        );
    }
}
