package com.expense.tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Outgoing response body for a Budget.
 */
@Data
@Builder
public class BudgetResponse {

    private Long id;
    private String month;
    private BigDecimal limitAmount;
    private Long categoryId;
    private String categoryName;
}
