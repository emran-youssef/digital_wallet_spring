package com.project.digital_wallet_with_spring.exceptions.transactionHistory;

public class HistoryNotFoundException extends RuntimeException{

    public HistoryNotFoundException() {
        super("There's no history found!");
    }
}
