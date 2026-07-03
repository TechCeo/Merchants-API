package com.portfolio.merchantapi.merchant;

public record MerchantResponse(
        Long id,
        String merchantName,
        String merchantAddress
) {
    public static MerchantResponse from(Merchant merchant) {
        return new MerchantResponse(
                merchant.getId(),
                merchant.getMerchantName(),
                merchant.getMerchantAddress()
        );
    }
}
