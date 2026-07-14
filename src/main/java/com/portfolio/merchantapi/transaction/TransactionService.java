package com.portfolio.merchantapi.transaction;

import com.portfolio.merchantapi.common.exception.DuplicateIdempotencyKeyException;
import com.portfolio.merchantapi.common.exception.InvalidIdempotencyKeyException;
import com.portfolio.merchantapi.common.exception.MerchantNotFoundException;
import com.portfolio.merchantapi.merchant.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final IdempotencyRecordRepository idempotencyRecordRepository;
    private final MerchantService merchantService;

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request, String idempotencyKey) {
        validateIdempotencyKey(idempotencyKey);
        if (!merchantService.existsById(request.merchantId())) {
            throw new MerchantNotFoundException(request.merchantId());
        }

        IdempotencyRecord idempotencyRecord = createIdempotencyRecord(idempotencyKey);
        MerchantTransaction transaction = new MerchantTransaction();
        transaction.setTransactionAmount(request.transactionAmount());
        transaction.setTransactionType(request.transactionType());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setMerchantId(request.merchantId());
        transaction.setConnectionMode(request.connectionMode());
        transaction.setTransactionStatus("New");

        MerchantTransaction savedTransaction = transactionRepository.save(transaction);
        idempotencyRecord.setTransactionId(savedTransaction.getId());
        idempotencyRecordRepository.save(idempotencyRecord);

        return TransactionResponse.from(savedTransaction);
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

    private IdempotencyRecord createIdempotencyRecord(String idempotencyKey) {
        IdempotencyRecord record = new IdempotencyRecord();
        record.setIdempotencyKey(idempotencyKey);
        record.setCreatedAt(LocalDateTime.now());

        try {
            return idempotencyRecordRepository.saveAndFlush(record);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateIdempotencyKeyException(idempotencyKey);
        }
    }

    private void validateIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new InvalidIdempotencyKeyException("Idempotency-Key header is required");
        }

        if (idempotencyKey.length() > 128) {
            throw new InvalidIdempotencyKeyException("Idempotency-Key must be 128 characters or less");
        }
    }
}
