package com.portfolio.merchantapi.common.exception;

public class DuplicateIdempotencyKeyException extends RuntimeException {

    public DuplicateIdempotencyKeyException(String idempotencyKey) {
        super("Duplicate idempotency key: " + idempotencyKey);
    }
}
