package com.project.digital_wallet_with_spring.exceptions.wallet;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException() {
        super("Wallet not found!");
    }
}
