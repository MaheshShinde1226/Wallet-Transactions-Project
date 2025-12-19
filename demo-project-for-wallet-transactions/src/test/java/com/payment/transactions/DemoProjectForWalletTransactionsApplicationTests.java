package com.payment.transactions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DemoProjectForWalletTransactionsApplicationTests {

	@Test
	void contextLoads() {
	}

}
