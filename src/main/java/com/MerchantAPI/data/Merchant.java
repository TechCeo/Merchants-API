package com.MerchantAPI.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String merchantName;
    private String merchantAddress;


}
