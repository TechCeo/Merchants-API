package com.portfolio.merchantapi.transaction;

import com.portfolio.merchantapi.common.exception.MerchantNotFoundException;
import com.portfolio.merchantapi.merchant.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MerchantService merchantService;

    public TransactionResponse createTransaction(TransactionRequest request) {

        MerchantTransaction transaction = new MerchantTransaction();
        transaction.setTransactionAmount(request.transactionAmount());
        transaction.setTransactionType(request.transactionType());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setMerchantId(request.merchantId());
        transaction.setConnectionMode(request.connectionMode());
        transaction.setTransactionStatus("New");
        return TransactionResponse.from(transactionRepository.save(transaction));
    }

    public List<TransactionResponse> findTransactionsByMerchant(Long merchantId, String connectionMode) {
        if (!merchantService.existsById(merchantId)) {
            throw new MerchantNotFoundException(merchantId);
        }

        return findMerchantTransactions(merchantId, connectionMode).stream()
                .map(TransactionResponse::from)
                .toList();
    }

    public List<TransactionResponse> findTransactions(String connectionMode) {
        List<MerchantTransaction> transactions = isConnectionMode(connectionMode)
                ? transactionRepository.findByConnectionModeIgnoreCase(connectionMode)
                : transactionRepository.findAll();

        return transactions.stream()
                .map(TransactionResponse::from)
                .toList();
    }

    private List<MerchantTransaction> findMerchantTransactions(Long merchantId, String connectionMode) {
        if (isConnectionMode(connectionMode)) {
            return transactionRepository.findByMerchantIdAndConnectionModeIgnoreCase(merchantId, connectionMode);
        }

        return transactionRepository.findByMerchantId(merchantId);
    }

    private boolean isConnectionMode(String connectionMode) {
        return connectionMode != null
                && (connectionMode.equalsIgnoreCase("Live") || connectionMode.equalsIgnoreCase("Test"));
    }
}
