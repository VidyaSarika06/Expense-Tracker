package com.expense.tracker.service;

import com.expense.tracker.dto.BudgetAlertDTO;
import com.expense.tracker.dto.MonthlySummaryResponse;

import java.util.List;

/**
 * DashboardService — defines the contract for all dashboard aggregate operations.
 */
public interface DashboardService {

    // Returns income, expense, balance + category breakdown + monthly trend
    MonthlySummaryResponse getMonthlySummary(String month);

    // Returns only categories where spending exceeded the budget
    List<BudgetAlertDTO> getBudgetAlerts(String month);
}
