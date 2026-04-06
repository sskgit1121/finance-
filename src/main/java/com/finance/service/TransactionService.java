package com.finance.service;


// src/main/java/com/finance/service/TransactionService.java

import com.finance.dto.request.TransactionRequest;
import com.finance.dto.response.TransactionResponse;
import com.finance.model.Role;
import com.finance.model.Transaction;
import com.finance.model.User;
import com.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public TransactionResponse createTransaction(Long userId, TransactionRequest request, User currentUser) {
        log.info("Creating transaction for user: {}", userId);

        // ACCESS CONTROL: Check permissions
        if (currentUser.getRole() == Role.VIEWER) {
            throw new RuntimeException("VIEWERS cannot create transactions");
        }

        if (currentUser.getRole() == Role.ANALYST && !currentUser.getId().equals(userId)) {
            throw new RuntimeException("ANALYSTS can only create transactions for themselves");
        }

        // Verify user exists
        userService.getUserById(userId);

        // Business validation
        if ("EXPENSE".equals(request.getType()) && request.getAmount().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Expense amount cannot be negative");
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setDescription(request.getDescription());
        transaction.setNotes(request.getNotes());
        transaction.setCreatedAt(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);
        log.info("Transaction created with ID: {}", saved.getId());

        return mapToResponse(saved);
    }

    public List<TransactionResponse> getTransactions(Long userId, User currentUser, String category, String type) {
        log.info("Fetching transactions for user: {}", userId);

        // ACCESS CONTROL: Check what data user can see
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(userId)) {
            throw new RuntimeException("You can only view your own transactions");
        }

        List<Transaction> transactions;
        if (category != null || type != null) {
            transactions = transactionRepository.findByFilters(userId, category, type);
        } else {
            transactions = transactionRepository.findByUserIdAndDeletedFalseOrderByTransactionDateDesc(userId);
        }

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse updateTransaction(Long transactionId, TransactionRequest request, User currentUser) {
        log.info("Updating transaction: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // ACCESS CONTROL: Check permissions
        if (currentUser.getRole() == Role.VIEWER) {
            throw new RuntimeException("VIEWERS cannot update transactions");
        }

        if (currentUser.getRole() == Role.ANALYST && !currentUser.getId().equals(transaction.getUserId())) {
            throw new RuntimeException("ANALYSTS can only update their own transactions");
        }

        // Update fields
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setDescription(request.getDescription());
        transaction.setNotes(request.getNotes());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction updated = transactionRepository.save(transaction);
        log.info("Transaction updated: {}", transactionId);

        return mapToResponse(updated);
    }

    public void deleteTransaction(Long transactionId, User currentUser) {
        log.info("Deleting transaction: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // ACCESS CONTROL: Only admins can delete
        if (currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMINS can delete transactions");
        }

        // Soft delete
        transaction.setDeleted(true);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        log.info("Transaction deleted (soft): {}", transactionId);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .category(transaction.getCategory())
                .transactionDate(transaction.getTransactionDate())
                .description(transaction.getDescription())
                .notes(transaction.getNotes())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
