package com.project.digital_wallet_with_spring.exceptions.wallet;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException()
    {
        super("Insufficient balance!");
    }
}
