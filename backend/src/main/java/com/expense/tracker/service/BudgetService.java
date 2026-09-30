package com.expense.tracker.service;

import com.expense.tracker.dto.BudgetRequest;
import com.expense.tracker.dto.BudgetResponse;

import java.util.List;

/**
 * BudgetService — defines the contract for all budget business operations.
 */
public interface BudgetService {

    BudgetResponse create(BudgetRequest request);

    List<BudgetResponse> getAll();

    BudgetResponse getById(Long id);

    BudgetResponse update(Long id, BudgetRequest request);

    void delete(Long id);
}
