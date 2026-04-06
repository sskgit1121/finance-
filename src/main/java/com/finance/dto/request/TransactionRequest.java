package com.finance.dto.request;


// src/main/java/com/finance/dto/request/TransactionRequest.java

import jakarta.validation.constraints.*;
        import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "999999999.99", message = "Amount too large")
    private BigDecimal amount;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "INCOME|EXPENSE", message = "Type must be INCOME or EXPENSE")
    private String type;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category too long")
    private String category;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate transactionDate;

    @Size(max = 255, message = "Description too long")
    private String description;

    private String notes;
}
