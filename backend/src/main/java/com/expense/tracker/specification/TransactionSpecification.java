package com.expense.tracker.specification;

import com.expense.tracker.entity.Transaction;
import com.expense.tracker.enums.TransactionType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    private TransactionSpecification() {}


    public static Specification<Transaction> withFilters(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            Long categoryId,
            TransactionType type) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always restrict to the logged-in user's transactions
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), endDate));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
