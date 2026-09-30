package com.expense.tracker.service.impl;

import com.expense.tracker.dto.CategoryRequest;
import com.expense.tracker.dto.CategoryResponse;
import com.expense.tracker.entity.Category;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.DuplicateResourceException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.CategoryMapper;
import com.expense.tracker.repository.CategoryRepository;
import com.expense.tracker.service.CategoryService;
import com.expense.tracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        User user = securityUtil.getLoggedInUser();

        // Duplicate name check is scoped to this user only
        if (categoryRepository.findByNameIgnoreCaseAndUser(request.getName(), user).isPresent()) {
            throw new DuplicateResourceException("Category name already exists: " + request.getName());
        }

        Category saved = categoryRepository.save(categoryMapper.toEntity(request, user));
        return categoryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        User user = securityUtil.getLoggedInUser();
        return categoryRepository.findByUser(user)
                .stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        return categoryMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        User user = securityUtil.getLoggedInUser();
        Category existing = findOrThrow(id);

        // Duplicate name check scoped to this user, excluding the current record
        if (categoryRepository.existsByNameIgnoreCaseAndIdNotAndUser(request.getName(), id, user)) {
            throw new DuplicateResourceException("Category name already exists: " + request.getName());
        }

        existing.setName(request.getName().trim());
        existing.setDescription(request.getDescription());

        return categoryMapper.toResponse(categoryRepository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findOrThrow(id);
        categoryRepository.deleteById(id);
    }

    private Category findOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
