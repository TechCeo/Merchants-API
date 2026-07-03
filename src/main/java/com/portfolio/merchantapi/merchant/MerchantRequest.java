package com.portfolio.merchantapi.merchant;

public record MerchantRequest(
        String merchantName,
        String merchantAddress
) {
}
