package com.expense.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginResponse — returned after a successful login.
 *
 * Contains:
 *   token → the JWT the client must send in every subsequent request
 *   email → logged-in user's email (for client-side display)
 *   role  → user's role (for client-side UI decisions)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String email;
    private String role;
}
