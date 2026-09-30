package com.expense.tracker.dto;

import java.math.BigDecimal;

public interface MonthlyTrendProjection {
    String getMonth();
    BigDecimal getIncome();
    BigDecimal getExpense();
}