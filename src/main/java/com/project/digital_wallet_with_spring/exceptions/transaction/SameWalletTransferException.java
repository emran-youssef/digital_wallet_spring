package com.project.digital_wallet_with_spring.exceptions.transaction;

public class SameWalletTransferException extends RuntimeException {
    public SameWalletTransferException() {
        super("You can't transfer money to your wallet");
    }
}
