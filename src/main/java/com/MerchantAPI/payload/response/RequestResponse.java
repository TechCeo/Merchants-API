package com.MerchantAPI.payload.response;

import lombok.Data;

@Data
public class RequestResponse {
    private String requestStatus;
    private Object response;
}
