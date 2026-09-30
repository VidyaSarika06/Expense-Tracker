package com.expense.tracker.service.impl;

import com.expense.tracker.dto.BudgetAlertDTO;
import com.expense.tracker.dto.CategoryExpenseDTO;
import com.expense.tracker.dto.MonthlyTrendDTO;
import com.expense.tracker.dto.MonthlyTrendProjection;
import com.expense.tracker.dto.MonthlySummaryResponse;
import com.expense.tracker.entity.Budget;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.BudgetRepository;
import com.expense.tracker.repository.TransactionRepository;
import com.expense.tracker.service.DashboardService;
import com.expense.tracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public MonthlySummaryResponse getMonthlySummary(String month) {
        User user = securityUtil.getLoggedInUser();
        Long userId = user.getId();

        BigDecimal totalIncome  = transactionRepository.sumIncomeByMonth(month, userId);
        BigDecimal totalExpense = transactionRepository.sumExpenseByMonth(month, userId);
        BigDecimal balance      = totalIncome.subtract(totalExpense);

        List<CategoryExpenseDTO> expenseByCategory =
                transactionRepository.expensesByCategoryForMonth(month, userId);

        // Fetch projections and map to DTOs
        List<MonthlyTrendProjection> trendProjections = transactionRepository.monthlyTrend(userId);
        List<MonthlyTrendDTO> trend = trendProjections.stream()
                .map(p -> new MonthlyTrendDTO(
                        p.getMonth(),
                        p.getIncome() == null ? BigDecimal.ZERO : p.getIncome(),
                        p.getExpense() == null ? BigDecimal.ZERO : p.getExpense()
                ))
                .collect(Collectors.toList());

        return MonthlySummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .expenseByCategory(expenseByCategory)
                .monthlyTrend(trend)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetAlertDTO> getBudgetAlerts(String month) {
        User user = securityUtil.getLoggedInUser();
        Long userId = user.getId();

        // Only this user's budgets for the given month
        List<Budget> budgets = budgetRepository.findByMonthAndUser(month, user);

        // Only this user's actual spending for the given month
        List<Object[]> actualRows = transactionRepository.actualSpendingByCategoryForMonth(month, userId);

        Map<String, BigDecimal> spendingMap = new HashMap<>();
        for (Object[] row : actualRows) {
            spendingMap.put((String) row[0], (BigDecimal) row[1]);
        }

        List<BudgetAlertDTO> alerts = new ArrayList<>();
        for (Budget budget : budgets) {
            String categoryName = budget.getCategory().getName();
            BigDecimal spent    = spendingMap.getOrDefault(categoryName, BigDecimal.ZERO);
            BigDecimal overrun  = spent.subtract(budget.getLimitAmount());

            if (overrun.compareTo(BigDecimal.ZERO) > 0) {
                alerts.add(new BudgetAlertDTO(categoryName, budget.getLimitAmount(), spent, overrun));
            }
        }

        return alerts;
    }
}