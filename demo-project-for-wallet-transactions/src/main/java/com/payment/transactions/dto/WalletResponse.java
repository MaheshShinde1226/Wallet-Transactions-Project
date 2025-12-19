package com.payment.transactions.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class WalletResponse {
    private int statusCode;
    private String status;
    private String message;

    private UUID walletId;
    private long balance;
    private WalletRequest walletInfo;

    public WalletResponse(UUID walletId, long balance){
        this.walletId = walletId;
        this.balance = balance;
    }
}
