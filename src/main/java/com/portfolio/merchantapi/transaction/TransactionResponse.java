package com.portfolio.merchantapi.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Merchant transaction resource")
public record TransactionResponse(
        @Schema(example = "10")
        Long id,

        @Schema(example = "25.50")
        BigDecimal transactionAmount,

        @Schema(example = "2026-07-13T20:30:00")
        LocalDateTime transactionDate,

        @Schema(example = "1")
        Long merchantId,

        @Schema(example = "SALE")
        String transactionType,

        @Schema(example = "New")
        String transactionStatus,

        @Schema(example = "Live")
        String connectionMode
) {
    public static TransactionResponse from(MerchantTransaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionAmount(),
                transaction.getTransactionDate(),
                transaction.getMerchantId(),
                transaction.getTransactionType(),
                transaction.getTransactionStatus(),
                transaction.getConnectionMode()
        );
    }
}
