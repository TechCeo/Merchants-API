package com.portfolio.merchantapi.transaction;

import com.portfolio.merchantapi.merchant.MerchantRequest;
import com.portfolio.merchantapi.merchant.MerchantResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionIntegrationTest {

    private static final DockerImageName ORACLE_IMAGE = DockerImageName
            .parse("gvenzl/oracle-free:slim-faststart")
            .asCompatibleSubstituteFor("gvenzl/oracle-xe");

    @Container
    static final OracleContainer oracle = new OracleContainer(ORACLE_IMAGE)
            .withUsername("merchant_api")
            .withPassword("merchant_api");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureOracle(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", oracle::getJdbcUrl);
        registry.add("spring.datasource.username", oracle::getUsername);
        registry.add("spring.datasource.password", oracle::getPassword);
        registry.add("spring.flyway.locations", () -> "classpath:db/migration/oracle");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }

    @Test
    void createTransactionRejectsDuplicateIdempotencyKey() {
        MerchantResponse merchant = createMerchant();
        TransactionRequest request = new TransactionRequest(
                BigDecimal.valueOf(25.50),
                merchant.id(),
                "SALE",
                "Live"
        );

        HttpEntity<TransactionRequest> transactionEntity = new HttpEntity<>(request, idempotencyHeaders());

        ResponseEntity<TransactionResponse> firstResponse = restTemplate.postForEntity(
                url("/api/v1/transactions"),
                transactionEntity,
                TransactionResponse.class
        );
        ResponseEntity<String> duplicateResponse = restTemplate.postForEntity(
                url("/api/v1/transactions"),
                transactionEntity,
                String.class
        );

        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(firstResponse.getBody()).isNotNull();
        assertThat(firstResponse.getBody().merchantId()).isEqualTo(merchant.id());

        assertThat(duplicateResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(duplicateResponse.getBody()).contains("Duplicate idempotency key");
    }

    private MerchantResponse createMerchant() {
        MerchantRequest request = new MerchantRequest("Acme Market", "100 Main Street");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<MerchantResponse> response = restTemplate.postForEntity(
                url("/api/v1/merchants"),
                new HttpEntity<>(request, headers),
                MerchantResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        return response.getBody();
    }

    private HttpHeaders idempotencyHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Idempotency-Key", "txn-integration-0001");
        return headers;
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
