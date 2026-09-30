package com.expense.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Holds the total expense amount for a single category.
 * Used in the dashboard category-breakdown query.
 * Constructor must match the JPQL SELECT NEW expression exactly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryExpenseDTO {

    private String category; // category name
    private BigDecimal amount; // total expense amount for that category
}
