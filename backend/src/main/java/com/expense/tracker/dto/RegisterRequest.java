package com.expense.tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * RegisterRequest — incoming body for POST /api/auth/register.
 *
 * Validation rules:
 *   name     → must not be blank
 *   email    → must be a valid email format
 *   password → minimum 8 characters
 *   role     → must be ROLE_STUDENT or ROLE_INDIVIDUAL (defaults to ROLE_STUDENT if omitted)
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$", message = "Password must contain uppercase, lowercase, number and special character")
    private String password;

    /**
     * Role sent by the client.
     * Accepted values: ROLE_STUDENT, ROLE_INDIVIDUAL
     *
     * @Pattern rejects any value that is not one of the two allowed roles.
     * The service layer defaults this to ROLE_STUDENT when it is null/blank.
     */
    @Pattern(
        regexp = "^(ROLE_STUDENT|ROLE_INDIVIDUAL)$",
        message = "Role must be either ROLE_STUDENT or ROLE_INDIVIDUAL"
    )
    private String role;
}
