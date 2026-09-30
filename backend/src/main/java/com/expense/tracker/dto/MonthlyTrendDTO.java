package com.expense.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Holds income and expense totals for a single month.
 * Used in the month-over-month trend query.
 * Constructor must match the JPQL SELECT NEW expression exactly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTrendDTO {

    private String month;        // "yyyy-MM" e.g. "2025-01"
    private BigDecimal income;   // total income for that month
    private BigDecimal expense;  // total expense for that month
}
