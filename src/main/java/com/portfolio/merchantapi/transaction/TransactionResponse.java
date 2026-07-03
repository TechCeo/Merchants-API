package com.portfolio.merchantapi.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        BigDecimal transactionAmount,
        LocalDateTime transactionDate,
        Long merchantId,
        String transactionType,
        String transactionStatus,
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
