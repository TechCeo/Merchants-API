package com.portfolio.merchantapi.common.exception;

public class MerchantNotFoundException extends RuntimeException {

    public MerchantNotFoundException(Long merchantId) {
        super("Merchant not found: " + merchantId);
    }
}
