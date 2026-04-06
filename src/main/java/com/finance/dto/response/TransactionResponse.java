package com.finance.dto.response;


// src/main/java/com/finance/dto/response/TransactionResponse.java

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String type;
    private String category;
    private LocalDate transactionDate;
    private String description;
    private String notes;
    private LocalDateTime createdAt;
}
