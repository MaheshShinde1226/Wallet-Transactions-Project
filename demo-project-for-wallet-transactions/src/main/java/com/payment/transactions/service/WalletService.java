package com.payment.transactions.service;

import com.payment.transactions.dto.OperationType;
import com.payment.transactions.dto.WalletRequest;
import com.payment.transactions.dto.WalletResponse;
import com.payment.transactions.entity.Wallet;
import com.payment.transactions.exceptions.InsufficientFundsException;
import com.payment.transactions.exceptions.WalletNotFoundException;
import com.payment.transactions.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Slf4j
public class WalletService {
    @Autowired
    private WalletRepository repository;

    @Transactional
    public WalletResponse processOperation(UUID walletId, OperationType type, long amount) {
        log.info("Inside WalletService | processOperation");
        WalletResponse res = new WalletResponse();
        try{
            Wallet wallet = repository.findById(walletId)
                    .orElseThrow(WalletNotFoundException::new);

            if (type == OperationType.WITHDRAW) {
                if (wallet.getBalance() < amount) {
                    throw new InsufficientFundsException();
                }
                wallet.setBalance(wallet.getBalance() - amount);
            } else {
                wallet.setBalance(wallet.getBalance() + amount);
            }

            repository.save(wallet);
            res.setStatus("success");
            res.setStatusCode(200);
            res.setMessage("Transaction Successful!");
        } catch (Exception e) {
            throw new InsufficientFundsException();
        }
        return res;
    }

    @Transactional(readOnly = true)
    public WalletResponse getWallet(UUID walletId) {
        log.info("Inside WalletService | getWallet");
        WalletResponse res = new WalletResponse();
        try {
            Wallet wallet = repository.findById(walletId)
                    .orElseThrow(() -> new WalletNotFoundException());
            WalletRequest dto = new WalletRequest();
            dto.setWalletId(wallet.getId());
            dto.setAmount(wallet.getBalance());
            dto.setVersion(wallet.getVersion());

            res.setStatus("success");
            res.setStatusCode(200);
            res.setWalletInfo(dto);
        } catch (Exception e) {
            throw new WalletNotFoundException();
        }
        return res;
    }
}
