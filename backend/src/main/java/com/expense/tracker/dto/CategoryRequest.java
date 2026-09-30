package com.expense.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Incoming request body for creating or updating a Category.
 * Validation annotations ensure bad data is rejected before hitting the service.
 */
@Data
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description; // optional
}
