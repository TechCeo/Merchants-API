package com.portfolio.merchantapi.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<MerchantTransaction,Long> {

    List<MerchantTransaction> findByMerchantId(Long merchantId);

    List<MerchantTransaction> findByMerchantIdAndConnectionModeIgnoreCase(Long merchantId, String connectionMode);

    List<MerchantTransaction> findByConnectionModeIgnoreCase(String connectionMode);
}
