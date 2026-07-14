package com.portfolio.merchantapi.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for creating a merchant")
public record MerchantRequest(
        @NotBlank(message = "merchantName is required")
        @Size(max = 255, message = "merchantName must be 255 characters or less")
        @Schema(description = "Display name of the merchant", example = "Acme Market")
        String merchantName,

        @NotBlank(message = "merchantAddress is required")
        @Size(max = 255, message = "merchantAddress must be 255 characters or less")
        @Schema(description = "Business address of the merchant", example = "100 Main Street, Austin, TX")
        String merchantAddress
) {
}
