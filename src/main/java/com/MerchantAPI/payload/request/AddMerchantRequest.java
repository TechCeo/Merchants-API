package com.MerchantAPI.payload.request;

import lombok.Data;

@Data
public class AddMerchantRequest {
    private String merchantName;
    private String merchantAddress;
}
