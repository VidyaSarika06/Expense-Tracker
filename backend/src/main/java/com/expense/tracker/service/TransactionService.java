package com.expense.tracker.service;

import com.expense.tracker.dto.TransactionRequest;
import com.expense.tracker.dto.TransactionResponse;
import com.expense.tracker.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * TransactionService — defines the contract for all transaction business operations.
 */
public interface TransactionService {

    TransactionResponse create(TransactionRequest request);

    TransactionResponse getById(Long id);

    // Paginated + filtered list
    Page<TransactionResponse> getAll(LocalDate startDate, LocalDate endDate,
                                     Long categoryId, TransactionType type,
                                     Pageable pageable);

    TransactionResponse update(Long id, TransactionRequest request);

    void delete(Long id);
}
