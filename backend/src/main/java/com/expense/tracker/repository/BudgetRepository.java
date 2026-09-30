package com.expense.tracker.repository;

import com.expense.tracker.entity.Budget;
import com.expense.tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // Fetch all budgets for a specific month belonging to the given user
    List<Budget> findByMonthAndUser(String month, User user);

    // Fetch all budgets belonging to the given user
    List<Budget> findByUser(User user);
}
