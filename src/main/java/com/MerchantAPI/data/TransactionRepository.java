package com.MerchantAPI.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<MerchantTransaction,Long> {

    @Query(value = "SELECT * FROM Merchant_Transaction u WHERE u.merchant_Id = ?1 and u.connection_Mode = ?2",  nativeQuery = true)
    List<MerchantTransaction> findByMerchantId(Long merchantId, String connectionMode);

    @Query(value = "SELECT * FROM Merchant_Transaction u WHERE u.connection_Mode = ?1",  nativeQuery = true)
    List<MerchantTransaction> findByConnectionMode(String connectionMode);
}
