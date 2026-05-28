package com.project.digital_wallet_with_spring.services;

import com.project.digital_wallet_with_spring.dtos.transaction.TransactionResponseDto;
import com.project.digital_wallet_with_spring.dtos.transaction.TransferRequestDto;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    TransactionResponseDto transfer(TransferRequestDto requestDto);
    TransactionResponseDto getTransactionById(Long transactionId);
    List<TransactionResponseDto> getTransactionsByWalletId(Long walletId);
}
