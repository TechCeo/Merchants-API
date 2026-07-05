package com.portfolio.merchantapi.merchant;

import com.portfolio.merchantapi.common.exception.GlobalExceptionHandler;
import com.portfolio.merchantapi.common.exception.MerchantNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MerchantController.class)
@Import(GlobalExceptionHandler.class)
class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MerchantService merchantService;

    @Test
    void createMerchantReturnsCreated() throws Exception {
        when(merchantService.createMerchant(any(MerchantRequest.class)))
                .thenReturn(new MerchantResponse(1L, "Acme Market", "100 Main Street"));

        mockMvc.perform(post("/api/v1/merchants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "merchantName": "Acme Market",
                                  "merchantAddress": "100 Main Street"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.merchantName").value("Acme Market"));
    }

    @Test
    void createMerchantReturnsBadRequestForInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/merchants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "merchantName": "",
                                  "merchantAddress": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid request"))
                .andExpect(jsonPath("$.errors.merchantName").value("merchantName is required"))
                .andExpect(jsonPath("$.errors.merchantAddress").value("merchantAddress is required"));
    }

    @Test
    void getMerchantReturnsNotFoundForMissingMerchant() throws Exception {
        when(merchantService.findMerchantById(99L))
                .thenThrow(new MerchantNotFoundException(99L));

        mockMvc.perform(get("/api/v1/merchants/{merchantId}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Merchant not found"))
                .andExpect(jsonPath("$.detail").value("Merchant not found: 99"));
    }
}
