package com.project.digital_wallet_with_spring.serivces;


import com.project.digital_wallet_with_spring.dtos.wallet.AmountRequestDto;
import com.project.digital_wallet_with_spring.dtos.wallet.WalletResponseDto;

import java.math.BigDecimal;

public interface WalletService {

    BigDecimal getBalance(Long walletId);
    WalletResponseDto deposit(AmountRequestDto request);
    WalletResponseDto withdraw(AmountRequestDto request);
    WalletResponseDto getWalletByUserId(Long userId);

}
