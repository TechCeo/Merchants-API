package com.MerchantAPI.data;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class MerchantTransaction {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "merchant_transaction1", sequenceName = "merchant_transaction", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merchant_transactionId")
    @SequenceGenerator(name = "merchant_transaction", sequenceName = "merchant_transactionId")
    @Column(name = "id", nullable = false)
    private Long id;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    private Long merchantId;
    private String transactionType;
    private String transactionStatus;
    private String connectionMode;


}
