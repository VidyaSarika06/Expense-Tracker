package com.expense.tracker.mapper;

import com.expense.tracker.dto.CategoryRequest;
import com.expense.tracker.dto.CategoryResponse;
import com.expense.tracker.entity.Category;
import com.expense.tracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request, User user) {
        return Category.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .user(user)
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
