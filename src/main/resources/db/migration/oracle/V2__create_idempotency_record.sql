CREATE TABLE idempotency_record (
    idempotency_key VARCHAR2(128) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    transaction_id NUMBER(19, 0),
    CONSTRAINT pk_idempotency_record PRIMARY KEY (idempotency_key),
    CONSTRAINT fk_idempotency_record_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES merchant_transaction (id)
);

CREATE INDEX idx_idempotency_record_created_at
    ON idempotency_record (created_at);
