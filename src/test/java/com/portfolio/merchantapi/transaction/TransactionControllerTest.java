package com.portfolio.merchantapi.transaction;

import com.portfolio.merchantapi.common.exception.GlobalExceptionHandler;
import com.portfolio.merchantapi.common.exception.MerchantNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@Import(GlobalExceptionHandler.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void createTransactionReturnsCreated() throws Exception {
        when(transactionService.createTransaction(any(TransactionRequest.class)))
                .thenReturn(new TransactionResponse(
                        10L,
                        BigDecimal.valueOf(25.50),
                        LocalDateTime.of(2026, 7, 4, 12, 0),
                        1L,
                        "SALE",
                        "New",
                        "Live"
                ));

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "transactionAmount": 25.50,
                                  "merchantId": 1,
                                  "transactionType": "SALE",
                                  "connectionMode": "Live"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.merchantId").value(1))
                .andExpect(jsonPath("$.transactionType").value("SALE"));
    }

    @Test
    void createTransactionReturnsBadRequestForInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "transactionAmount": 0,
                                  "merchantId": -1,
                                  "transactionType": "",
                                  "connectionMode": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid request"))
                .andExpect(jsonPath("$.errors.transactionAmount").value("transactionAmount must be at least 0.01"))
                .andExpect(jsonPath("$.errors.merchantId").value("merchantId must be positive"))
                .andExpect(jsonPath("$.errors.transactionType").value("transactionType is required"))
                .andExpect(jsonPath("$.errors.connectionMode").value("connectionMode is required"));
    }

    @Test
    void getMerchantTransactionsReturnsNotFoundForMissingMerchant() throws Exception {
        when(transactionService.findTransactionsByMerchant(99L, null))
                .thenThrow(new MerchantNotFoundException(99L));

        mockMvc.perform(get("/api/v1/merchants/{merchantId}/transactions", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Merchant not found"))
                .andExpect(jsonPath("$.detail").value("Merchant not found: 99"));
    }
}
