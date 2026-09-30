package com.expense.tracker.service.impl;

import com.expense.tracker.dto.BudgetRequest;
import com.expense.tracker.dto.BudgetResponse;
import com.expense.tracker.entity.Budget;
import com.expense.tracker.entity.Category;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.BudgetMapper;
import com.expense.tracker.repository.BudgetRepository;
import com.expense.tracker.repository.CategoryRepository;
import com.expense.tracker.service.BudgetService;
import com.expense.tracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetMapper budgetMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public BudgetResponse create(BudgetRequest request) {
        User user = securityUtil.getLoggedInUser();
        Category category = findCategoryOrThrow(request.getCategoryId());
        Budget saved = budgetRepository.save(budgetMapper.toEntity(request, category, user));
        return budgetMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetResponse> getAll() {
        User user = securityUtil.getLoggedInUser();
        return budgetRepository.findByUser(user)
                .stream()
                .map(budgetMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetResponse getById(Long id) {
        return budgetMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional
    public BudgetResponse update(Long id, BudgetRequest request) {
        Budget existing = findOrThrow(id);
        Category category = findCategoryOrThrow(request.getCategoryId());

        existing.setMonth(request.getMonth());
        existing.setLimitAmount(request.getLimitAmount());
        existing.setCategory(category);
        // user is not changed on update — ownership is immutable

        return budgetMapper.toResponse(budgetRepository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findOrThrow(id);
        budgetRepository.deleteById(id);
    }

    private Budget findOrThrow(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));
    }

    private Category findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }
}
