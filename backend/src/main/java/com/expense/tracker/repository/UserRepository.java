package com.expense.tracker.repository;

import com.expense.tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository — provides DB access for the User entity.
 *
 * findByEmail is used in two places:
 *   1. CustomUserDetailsService → loads user during JWT validation
 *   2. AuthServiceImpl          → checks for duplicate email on register
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Used to check if email is already taken before registering
    boolean existsByEmail(String email);
}
