package com.expense.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a budget overrun alert for a single category.
 * Only returned when actual spending exceeds the budget limit.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetAlertDTO {

    private String category;    // category name
    private BigDecimal budget;  // the set budget limit
    private BigDecimal spent;   // actual amount spent
    private BigDecimal overrun; // spent - budget (always positive here)
}
