package com.payment.transactions.controller;

import com.payment.transactions.dto.WalletRequest;
import com.payment.transactions.dto.WalletResponse;
import com.payment.transactions.service.WalletService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(name = "/wallet")
@Slf4j
public class WalletController {
    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    @PostMapping("/wallet")
    public WalletResponse changeBalance(@RequestBody @Valid WalletRequest request) {
        log.info("Inside WalletController | changeBalance");
        return service.processOperation(
                request.getWalletId(),
                request.getOperationType(),
                request.getAmount()
        );
    }

    @GetMapping("/wallets/{walletId}")
    public WalletResponse getWallet(@PathVariable UUID walletId) {
        log.info("Inside WalletController | getWallet");
        return service.getWallet(walletId);
    }
}
