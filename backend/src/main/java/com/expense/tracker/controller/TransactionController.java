package com.expense.tracker.controller;

import com.expense.tracker.dto.TransactionRequest;
import com.expense.tracker.dto.TransactionResponse;
import com.expense.tracker.enums.TransactionType;
import com.expense.tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * TransactionController — exposes REST endpoints for Transaction CRUD + filtered list.
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // POST /api/transactions
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(request));
    }

    // GET /api/transactions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    /**
     * GET /api/transactions
     * Supports optional query params:
     *   startDate, endDate  — filter by date range (format: yyyy-MM-dd)
     *   categoryId          — filter by category
     *   type                — filter by INCOME or EXPENSE
     *   page, size          — pagination (default: page=0, size=10)
     *   sortBy, sortDir     — sorting (default: date DESC)
     */
    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "10")  int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        // Build Sort direction from the sortDir parameter
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                transactionService.getAll(startDate, endDate, categoryId, type, pageable));
    }

    // PUT /api/transactions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.update(id, request));
    }

    // DELETE /api/transactions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
