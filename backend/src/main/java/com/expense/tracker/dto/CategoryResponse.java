package com.expense.tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Outgoing response body for Category.
 * We never expose the entity directly — DTOs decouple the API from the DB model.
 */
@Data
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
