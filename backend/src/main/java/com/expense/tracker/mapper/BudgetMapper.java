package com.expense.tracker.mapper;

import com.expense.tracker.dto.BudgetRequest;
import com.expense.tracker.dto.BudgetResponse;
import com.expense.tracker.entity.Budget;
import com.expense.tracker.entity.Category;
import com.expense.tracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

    public Budget toEntity(BudgetRequest request, Category category, User user) {
        return Budget.builder()
                .month(request.getMonth())
                .limitAmount(request.getLimitAmount())
                .category(category)
                .user(user)
                .build();
    }

    public BudgetResponse toResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .month(budget.getMonth())
                .limitAmount(budget.getLimitAmount())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .build();
    }
}
