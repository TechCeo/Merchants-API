package com.MerchantAPI.payload.request;

import lombok.Data;

@Data
public class FetchTransactionRequest {
    private Long merchantId;
    private String connectionMode;
}
