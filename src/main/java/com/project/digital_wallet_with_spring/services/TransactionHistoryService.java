package com.project.digital_wallet_with_spring.services;

import com.project.digital_wallet_with_spring.dtos.transactionHistory.TransactionHistoryResponseDto;
import com.project.digital_wallet_with_spring.enums.TransactionType;

import java.util.List;

public interface TransactionHistoryService {

    List<TransactionHistoryResponseDto> getHistoryByEmail(String email);
    List<TransactionHistoryResponseDto> getHistoryByWalletId(Long walletId);
    List<TransactionHistoryResponseDto> getHistoryByWalletIdAndType(Long walletId, TransactionType type);


}
