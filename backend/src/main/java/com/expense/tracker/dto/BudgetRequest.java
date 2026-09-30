package com.expense.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Incoming request body for creating or updating a Budget.
 */
@Data
public class BudgetRequest {

    // Month in "yyyy-MM" format e.g. "2025-06"
    @NotBlank(message = "Month is required (format: yyyy-MM)")
    private String month;

    @NotNull(message = "Limit amount is required")
    @Positive(message = "Limit amount must be greater than 0")
    private BigDecimal limitAmount;

    @NotNull(message = "Category is required")
    private Long categoryId;
}
