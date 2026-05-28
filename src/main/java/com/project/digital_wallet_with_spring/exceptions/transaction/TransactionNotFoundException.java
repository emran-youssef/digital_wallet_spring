package com.project.digital_wallet_with_spring.exceptions.transaction;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException() {
        super("Transaction not found!");
    }
}
