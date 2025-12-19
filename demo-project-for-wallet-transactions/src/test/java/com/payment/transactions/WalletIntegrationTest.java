package com.payment.transactions;

import com.payment.transactions.dto.OperationType;
import com.payment.transactions.dto.WalletRequest;
import com.payment.transactions.dto.WalletResponse;
import com.payment.transactions.entity.Wallet;
import com.payment.transactions.repository.WalletRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WalletRepository walletRepository;


    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("wallet")
                    .withUsername("wallet")
                    .withPassword("wallet");

    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {
        walletRepository.deleteAll();

        Wallet wallet = new Wallet();
        wallet.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        wallet.setBalance(1000L);

        walletRepository.save(wallet);
    }

    @Test
    public void shouldDepositMoney() {
        UUID walletId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        WalletRequest request = new WalletRequest(
                walletId,
                OperationType.DEPOSIT,
                500
        );

        ResponseEntity<WalletResponse> response =
                restTemplate.postForEntity("/api/v1/wallet", request, WalletResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(200);

        Wallet wallet = walletRepository.findById(walletId).orElseThrow();
        assertThat(wallet.getBalance()).isEqualTo(1500);
    }

    @Test
    public void shouldWithdrawMoney() {
        UUID walletId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        WalletRequest request = new WalletRequest(
                walletId,
                OperationType.WITHDRAW,
                300
        );

        ResponseEntity<WalletResponse> response =
                restTemplate.postForEntity("/api/v1/wallet", request, WalletResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(200);

        Wallet wallet = walletRepository.findById(walletId).orElseThrow();
        assertThat(wallet.getBalance()).isEqualTo(700);
    }

    @Test
    public void shouldFailWhenInsufficientFunds() {
        UUID walletId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        WalletRequest request = new WalletRequest(
                walletId,
                OperationType.WITHDRAW,
                5000
        );

        ResponseEntity<WalletResponse> response =
                restTemplate.postForEntity("/api/v1/wallet", request, WalletResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(402);
        assertThat(response.getBody().getStatus()).isEqualTo("INSUFFICIENT_FUNDS");
    }

    @Test
    public void shouldReturnWalletBalance() {
        UUID walletId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        ResponseEntity<WalletResponse> response =
                restTemplate.getForEntity(
                        "/api/v1/wallets/" + walletId,
                        WalletResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().getBalance()).isEqualTo(1000);
    }

    @Test
    public void shouldHandleConcurrentDeposits() throws Exception {
        UUID walletId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        ExecutorService executor = Executors.newFixedThreadPool(10);

        WalletRequest request = new WalletRequest(
                walletId,
                OperationType.DEPOSIT,
                10
        );

        for (int i = 0; i < 100; i++) {
            executor.submit(() ->
                    restTemplate.postForEntity("/api/v1/wallet", request, Void.class)
            );
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        Wallet wallet = walletRepository.findById(walletId).orElseThrow();
        assertThat(wallet.getBalance()).isEqualTo(1000 + 100 * 10);
    }


}
