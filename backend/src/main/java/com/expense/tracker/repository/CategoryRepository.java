package com.expense.tracker.repository;

import com.expense.tracker.entity.Category;
import com.expense.tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Return only categories belonging to the given user
    List<Category> findByUser(User user);

    // Duplicate name check scoped to the same user
    Optional<Category> findByNameIgnoreCaseAndUser(String name, User user);

    // Duplicate name check on update — exclude current record by id, scoped to user
    boolean existsByNameIgnoreCaseAndIdNotAndUser(String name, Long id, User user);
}
