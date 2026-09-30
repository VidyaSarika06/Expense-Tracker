package com.expense.tracker.mapper;

import com.expense.tracker.dto.TransactionRequest;
import com.expense.tracker.dto.TransactionResponse;
import com.expense.tracker.entity.Category;
import com.expense.tracker.entity.Transaction;
import com.expense.tracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequest request, Category category, User user) {
        return Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .date(request.getDate())
                .note(request.getNote())
                .category(category)
                .user(user)
                .build();
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .date(transaction.getDate())
                .note(transaction.getNote())
                .categoryId(transaction.getCategory().getId())
                .categoryName(transaction.getCategory().getName())
                .build();
    }
}
