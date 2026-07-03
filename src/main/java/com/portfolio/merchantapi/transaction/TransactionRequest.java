package com.portfolio.merchantapi.transaction;

import java.math.BigDecimal;

public record TransactionRequest(
        BigDecimal transactionAmount,
        Long merchantId,
        String transactionType,
        String connectionMode
) {
}
