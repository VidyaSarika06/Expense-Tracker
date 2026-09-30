package com.expense.tracker.repository;

import com.expense.tracker.dto.CategoryExpenseDTO;
import com.expense.tracker.dto.MonthlyTrendDTO;
import com.expense.tracker.dto.MonthlyTrendProjection;
import com.expense.tracker.entity.Transaction;
import com.expense.tracker.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {

    // Total income for a given month — scoped to user
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.type = 'INCOME' " +
            "AND CONCAT(YEAR(t.date), '-', LPAD(CAST(MONTH(t.date) AS string), 2, '0')) = :month " +
            "AND t.user.id = :userId")
    BigDecimal sumIncomeByMonth(@Param("month") String month, @Param("userId") Long userId);

    // Total expense for a given month — scoped to user
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.type = 'EXPENSE' " +
            "AND CONCAT(YEAR(t.date), '-', LPAD(CAST(MONTH(t.date) AS string), 2, '0')) = :month " +
            "AND t.user.id = :userId")
    BigDecimal sumExpenseByMonth(@Param("month") String month, @Param("userId") Long userId);

    // Expense grouped by category for a given month — scoped to user
    @Query("SELECT new com.expense.tracker.dto.CategoryExpenseDTO(t.category.name, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.type = 'EXPENSE' " +
            "AND CONCAT(YEAR(t.date), '-', LPAD(CAST(MONTH(t.date) AS string), 2, '0')) = :month " +
            "AND t.user.id = :userId " +
            "GROUP BY t.category.name")
    List<CategoryExpenseDTO> expensesByCategoryForMonth(@Param("month") String month,
                                                        @Param("userId") Long userId);

    // Month-over-month income and expense trend — native query, scoped to user
    @Query(value =
            "SELECT DATE_FORMAT(t.date, '%Y-%m') AS month, " +
                    "       SUM(CASE WHEN t.type = 'INCOME'  THEN t.amount ELSE 0 END) AS income, " +
                    "       SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) AS expense " +
                    "FROM `transaction` t " +
                    "WHERE t.user_id = :userId " +
                    "GROUP BY DATE_FORMAT(t.date, '%Y-%m') " +
                    "ORDER BY DATE_FORMAT(t.date, '%Y-%m')",
            nativeQuery = true)
    List<MonthlyTrendProjection> monthlyTrend(@Param("userId") Long userId);

    // Actual spending per category for a given month — scoped to user
    @Query("SELECT t.category.name, SUM(t.amount) FROM Transaction t " +
            "WHERE t.type = 'EXPENSE' " +
            "AND CONCAT(YEAR(t.date), '-', LPAD(CAST(MONTH(t.date) AS string), 2, '0')) = :month " +
            "AND t.user.id = :userId " +
            "GROUP BY t.category.name")
    List<Object[]> actualSpendingByCategoryForMonth(@Param("month") String month,
                                                    @Param("userId") Long userId);
}