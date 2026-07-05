package com.portfolio.merchantapi.merchant;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "merchant")
@Data
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "merchant_name")
    private String merchantName;
    @Column(name = "merchant_address")
    private String merchantAddress;
}
