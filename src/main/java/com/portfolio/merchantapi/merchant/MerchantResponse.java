package com.portfolio.merchantapi.merchant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Merchant resource")
public record MerchantResponse(
        @Schema(example = "1")
        Long id,

        @Schema(example = "Acme Market")
        String merchantName,

        @Schema(example = "100 Main Street, Austin, TX")
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
