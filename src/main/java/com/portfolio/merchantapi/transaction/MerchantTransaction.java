package com.portfolio.merchantapi.transaction;

import lombok.Data;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_transaction")
@Data
public class MerchantTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merchant_transaction_id_seq")
    @SequenceGenerator(name = "merchant_transaction_id_seq", sequenceName = "merchant_transaction_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "transaction_amount", precision = 19, scale = 2)
    private BigDecimal transactionAmount;
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "transaction_status")
    private String transactionStatus;
    @Column(name = "connection_mode")
    private String connectionMode;
}
