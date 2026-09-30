package com.expense.tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User entity — maps to the 'users' table.
 *
 * ROLE STORAGE CONVENTION:
 *   The role column stores the full Spring Security authority string:
 *
 *     "ROLE_STUDENT"    — registered as a student
 *     "ROLE_INDIVIDUAL" — registered as an individual
 *
 *   The value is used directly as a GrantedAuthority in CustomUserDetailsService.
 *   No "ROLE_" prefix is added at runtime — it is already part of the stored value.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Email is used as the login username — must be unique across the table
    @Column(nullable = false, unique = true)
    private String email;

    // BCrypt hashed password — never stored as plain text
    @Column(nullable = false)
    private String password;

    /**
     * Role stored as the full authority string.
     * Allowed values: ROLE_STUDENT, ROLE_INDIVIDUAL
     *
     * @Builder.Default ensures the Lombok builder uses this default
     * when .role() is not explicitly called on the builder.
     * The service layer always sets this explicitly from the request,
     * so this default acts only as a safety net.
     */
    @Column(nullable = false)
    @Builder.Default
    private String role = "ROLE_STUDENT";
}
