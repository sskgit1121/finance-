package com.finance.dto.response;


// src/main/java/com/finance/dto/response/DashboardSummaryResponse.java

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardSummaryResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netBalance;
    private Map<String, BigDecimal> categoryTotals;
    private List<TransactionResponse> recentTransactions;
    private Map<String, BigDecimal> monthlyIncome;
    private Map<String, BigDecimal> monthlyExpense;
    private int totalTransactionCount;
}
