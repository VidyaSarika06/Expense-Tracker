package com.expense.tracker.dto;

import com.expense.tracker.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Incoming request body for creating or updating a Transaction.
 */
@Data
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType type; // INCOME or EXPENSE

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate date;

    private String note; // optional

    @NotNull(message = "Category is required")
    private Long categoryId; // client sends category ID, not the full object
}
