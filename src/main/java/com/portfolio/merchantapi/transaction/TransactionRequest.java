package com.portfolio.merchantapi.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "transactionAmount is required")
        @DecimalMin(value = "0.01", message = "transactionAmount must be at least 0.01")
        BigDecimal transactionAmount,

        @NotNull(message = "merchantId is required")
        @Positive(message = "merchantId must be positive")
        Long merchantId,

        @NotBlank(message = "transactionType is required")
        @Size(max = 50, message = "transactionType must be 50 characters or less")
        String transactionType,

        @NotBlank(message = "connectionMode is required")
        @Size(max = 20, message = "connectionMode must be 20 characters or less")
        String connectionMode
) {
}
