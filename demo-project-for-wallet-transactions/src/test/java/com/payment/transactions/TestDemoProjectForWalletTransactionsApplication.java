package com.payment.transactions;

import org.springframework.boot.SpringApplication;

public class TestDemoProjectForWalletTransactionsApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoProjectForWalletTransactionsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
