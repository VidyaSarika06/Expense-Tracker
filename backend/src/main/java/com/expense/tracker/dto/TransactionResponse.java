package com.expense.tracker.dto;

import com.expense.tracker.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Outgoing response body for a Transaction.
 */
@Data
@Builder
public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate date;
    private String note;
    private Long categoryId;
    private String categoryName; // convenient to show category name directly
}
