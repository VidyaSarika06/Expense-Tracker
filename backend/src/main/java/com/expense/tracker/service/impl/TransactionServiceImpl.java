package com.expense.tracker.service.impl;

import com.expense.tracker.dto.TransactionRequest;
import com.expense.tracker.dto.TransactionResponse;
import com.expense.tracker.entity.Category;
import com.expense.tracker.entity.Transaction;
import com.expense.tracker.entity.User;
import com.expense.tracker.enums.TransactionType;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.TransactionMapper;
import com.expense.tracker.repository.CategoryRepository;
import com.expense.tracker.repository.TransactionRepository;
import com.expense.tracker.service.TransactionService;
import com.expense.tracker.specification.TransactionSpecification;
import com.expense.tracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        User user = securityUtil.getLoggedInUser();
        Category category = findCategoryOrThrow(request.getCategoryId());
        Transaction saved = transactionRepository.save(transactionMapper.toEntity(request, category, user));
        return transactionMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getById(Long id) {
        return transactionMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAll(LocalDate startDate, LocalDate endDate,
                                             Long categoryId, TransactionType type,
                                             Pageable pageable) {
        User user = securityUtil.getLoggedInUser();

        // userId is always injected — filters are applied only within the user's own records
        Specification<Transaction> spec = TransactionSpecification
                .withFilters(user.getId(), startDate, endDate, categoryId, type);

        return transactionRepository.findAll(spec, pageable)
                .map(transactionMapper::toResponse);
    }

    @Override
    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction existing = findOrThrow(id);
        Category category = findCategoryOrThrow(request.getCategoryId());

        existing.setAmount(request.getAmount());
        existing.setType(request.getType());
        existing.setDate(request.getDate());
        existing.setNote(request.getNote());
        existing.setCategory(category);
        // user is not changed on update — ownership is immutable

        return transactionMapper.toResponse(transactionRepository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findOrThrow(id);
        transactionRepository.deleteById(id);
    }

    private Transaction findOrThrow(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    private Category findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }
}
