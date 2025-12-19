package com.payment.transactions.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class WalletRequest {
    private UUID walletId;
    private OperationType operationType;
    private long amount;
    private long version;

    public WalletRequest(UUID walletId, OperationType operationType, long amount) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.amount = amount;
    }
}
