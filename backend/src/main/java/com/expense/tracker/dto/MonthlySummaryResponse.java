package com.expense.tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Top-level dashboard response for GET /api/dashboard/monthly-summary.
 * Aggregates income, expense, balance, category breakdown, and trend in one response.
 */
@Data
@Builder
public class MonthlySummaryResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance; // totalIncome - totalExpense

    // List of expenses grouped by category for the requested month
    private List<CategoryExpenseDTO> expenseByCategory;

    // Month-over-month trend (all months, not just the requested one)
    private List<MonthlyTrendDTO> monthlyTrend;
}
