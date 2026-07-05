package com.portfolio.merchantapi.merchant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MerchantRequest(
        @NotBlank(message = "merchantName is required")
        @Size(max = 255, message = "merchantName must be 255 characters or less")
        String merchantName,

        @NotBlank(message = "merchantAddress is required")
        @Size(max = 255, message = "merchantAddress must be 255 characters or less")
        String merchantAddress
) {
}
