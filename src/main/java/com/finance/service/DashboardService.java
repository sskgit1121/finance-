package com.finance.service;


// src/main/java/com/finance/service/DashboardService.java

import com.finance.dto.response.DashboardSummaryResponse;
import com.finance.dto.response.TransactionResponse;
import com.finance.model.Role;
import com.finance.model.Transaction;
import com.finance.model.User;
import com.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
        import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    public DashboardSummaryResponse getDashboardSummary(Long userId, User currentUser,
                                                        LocalDate startDate, LocalDate endDate) {
        log.info("Generating dashboard summary for user: {}", userId);

        // ACCESS CONTROL: Check if user can view this dashboard
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(userId)) {
            throw new RuntimeException("You can only view your own dashboard");
        }

        // Set default date range (last 30 days if not specified)
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        // Get transactions in date range
        List<Transaction> transactions = transactionRepository
                .findByUserIdAndDeletedFalseAndTransactionDateBetweenOrderByTransactionDateDesc(
                        userId, startDate, endDate);

        // Calculate totals
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        // Category-wise totals (only expenses)
        Map<String, BigDecimal> categoryTotals = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        // Sort categories by amount (descending)
        categoryTotals = categoryTotals.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        // Recent transactions (last 10)
        List<TransactionResponse> recentTransactions = transactions.stream()
                .limit(10)
                .map(t -> TransactionResponse.builder()
                        .id(t.getId())
                        .amount(t.getAmount())
                        .type(t.getType())
                        .category(t.getCategory())
                        .transactionDate(t.getTransactionDate())
                        .description(t.getDescription())
                        .build())
                .collect(Collectors.toList());

        // Monthly trends
        Map<String, BigDecimal> monthlyIncome = calculateMonthlyTrend(userId, "INCOME", startDate, endDate);
        Map<String, BigDecimal> monthlyExpense = calculateMonthlyTrend(userId, "EXPENSE", startDate, endDate);

        // Total transaction count
        int totalCount = (int) transactionRepository.countByUserIdAndDeletedFalse(userId);

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(netBalance)
                .categoryTotals(categoryTotals)
                .recentTransactions(recentTransactions)
                .monthlyIncome(monthlyIncome)
                .monthlyExpense(monthlyExpense)
                .totalTransactionCount(totalCount)
                .build();
    }

    private Map<String, BigDecimal> calculateMonthlyTrend(Long userId, String type,
                                                          LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository
                .findByUserIdAndDeletedFalseAndTransactionDateBetweenOrderByTransactionDateDesc(
                        userId, startDate, endDate);

        return transactions.stream()
                .filter(t -> type.equals(t.getType()))
                .collect(Collectors.groupingBy(
                        t -> t.getTransactionDate().getYear() + "-" + String.format("%02d", t.getTransactionDate().getMonthValue()),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));
    }
}
