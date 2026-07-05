package com.portfolio.merchantapi.transaction;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(request));
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions(@RequestParam(required = false) String connectionMode) {
        return transactionService.findTransactions(connectionMode);
    }

    @GetMapping("/merchants/{merchantId}/transactions")
    public List<TransactionResponse> getMerchantTransactions(
            @PathVariable Long merchantId,
            @RequestParam(required = false) String connectionMode) {

        return transactionService.findTransactionsByMerchant(merchantId, connectionMode);
    }
}
