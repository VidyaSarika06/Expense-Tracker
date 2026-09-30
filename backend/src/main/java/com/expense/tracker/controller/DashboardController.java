package com.expense.tracker.controller;

import com.expense.tracker.dto.BudgetAlertDTO;
import com.expense.tracker.dto.MonthlySummaryResponse;
import com.expense.tracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DashboardController — exposes read-only aggregate/summary endpoints.
 * No request body needed — all inputs come as query parameters.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/dashboard/monthly-summary?month=2025-06
     * Returns income, expense, balance, category breakdown, and monthly trend.
     */
    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @RequestParam String month) { // e.g. "2025-06"
        return ResponseEntity.ok(dashboardService.getMonthlySummary(month));
    }

    /**
     * GET /api/dashboard/budget-alerts?month=2025-06
     * Returns only categories where actual spending exceeded the budget.
     */
    @GetMapping("/budget-alerts")
    public ResponseEntity<List<BudgetAlertDTO>> getBudgetAlerts(
            @RequestParam String month) {
        return ResponseEntity.ok(dashboardService.getBudgetAlerts(month));
    }
}
