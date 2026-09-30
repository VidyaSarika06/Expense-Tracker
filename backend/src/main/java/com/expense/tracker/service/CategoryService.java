package com.expense.tracker.service;

import com.expense.tracker.dto.CategoryRequest;
import com.expense.tracker.dto.CategoryResponse;

import java.util.List;

/**
 * CategoryService — defines the contract for all category business operations.
 * The controller depends on this interface, not the implementation.
 */
public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    List<CategoryResponse> getAll();

    CategoryResponse getById(Long id);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
