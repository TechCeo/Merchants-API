package com.portfolio.merchantapi.merchant;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchants")
@Tag(name = "Merchants", description = "Merchant account management")
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping
    @Operation(summary = "Create a merchant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Merchant created",
                    content = @Content(schema = @Schema(implementation = MerchantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<MerchantResponse> createMerchant(@Valid @RequestBody MerchantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(merchantService.createMerchant(request));
    }

    @GetMapping("/{merchantId}")
    @Operation(summary = "Get a merchant by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Merchant found",
                    content = @Content(schema = @Schema(implementation = MerchantResponse.class))),
            @ApiResponse(responseCode = "404", description = "Merchant not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public MerchantResponse getMerchant(@PathVariable Long merchantId) {
        return merchantService.findMerchantById(merchantId);
    }
}
