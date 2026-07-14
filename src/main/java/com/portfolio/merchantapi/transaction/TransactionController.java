package com.portfolio.merchantapi.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Transactions", description = "Merchant transaction processing")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transactions")
    @Operation(summary = "Create a transaction", description = "Requires an Idempotency-Key header to prevent duplicate transaction creation.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction created",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or missing idempotency key",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Merchant not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate Idempotency-Key",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TransactionResponse> createTransaction(
            @Parameter(
                    name = "Idempotency-Key",
                    description = "Unique key for this transaction creation attempt. Reusing the key is rejected with 409 Conflict.",
                    required = true,
                    in = ParameterIn.HEADER,
                    example = "txn-20260713-0001"
            )
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody TransactionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(request, idempotencyKey));
    }

    @GetMapping("/transactions")
    @Operation(summary = "List transactions")
    @ApiResponse(responseCode = "200", description = "Transactions returned",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponse.class))))
    public List<TransactionResponse> getTransactions(@RequestParam(required = false) String connectionMode) {
        return transactionService.findTransactions(connectionMode);
    }

    @GetMapping("/merchants/{merchantId}/transactions")
    @Operation(summary = "List transactions for a merchant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions returned",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Merchant not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public List<TransactionResponse> getMerchantTransactions(
            @PathVariable Long merchantId,
            @RequestParam(required = false) String connectionMode) {

        return transactionService.findTransactionsByMerchant(merchantId, connectionMode);
    }
}
