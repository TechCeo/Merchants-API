package com.MerchantAPI.payload.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AddTransactionRequest {
    private BigDecimal transactionAmount;
    private Long merchantId;
    private String transactionType;
    private String connectionMode;
}
