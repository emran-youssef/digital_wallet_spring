package com.project.digital_wallet_with_spring.services;


import com.project.digital_wallet_with_spring.dtos.wallet.AmountRequestDto;
import com.project.digital_wallet_with_spring.dtos.wallet.WalletResponseDto;

import java.math.BigDecimal;

public interface WalletService {

    BigDecimal getBalance(Long walletId);
    WalletResponseDto deposit(Long walletId, BigDecimal amount);
    WalletResponseDto withdraw(Long walletId, BigDecimal amount);
    WalletResponseDto getWalletByUserId(Long userId);

}
