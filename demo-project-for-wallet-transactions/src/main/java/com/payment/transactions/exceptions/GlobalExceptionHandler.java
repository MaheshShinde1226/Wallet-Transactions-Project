package com.payment.transactions.exceptions;

import com.payment.transactions.dto.WalletResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public WalletResponse handleNotFound() {
        WalletResponse res = new WalletResponse();
        res.setStatus("Not Found");
        res.setStatusCode(404);
        res.setMessage("WALLET_NOT_FOUND");
        return res;
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public WalletResponse handleFunds() {
        WalletResponse res = new WalletResponse();
        res.setStatus("Bad Request");
        res.setStatusCode(402);
        res.setMessage("INSUFFICIENT_FUNDS");
        return res;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public WalletResponse handleValidation() {
        WalletResponse res = new WalletResponse();
        res.setStatus("Bad Request");
        res.setStatusCode(400);
        res.setMessage("INVALID_REQUEST");
        return res;
    }
}
