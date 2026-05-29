package com.project.digital_wallet_with_spring.servicesImp;

import com.project.digital_wallet_with_spring.dtos.wallet.AmountRequestDto;
import com.project.digital_wallet_with_spring.dtos.wallet.WalletResponseDto;
import com.project.digital_wallet_with_spring.entities.Wallet;
import com.project.digital_wallet_with_spring.exceptions.wallet.InsufficientBalanceException;
import com.project.digital_wallet_with_spring.exceptions.wallet.WalletNotFoundException;
import com.project.digital_wallet_with_spring.mappers.WalletMapper;
import com.project.digital_wallet_with_spring.repositories.WalletRepository;
import com.project.digital_wallet_with_spring.services.WalletService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalletServiceImp implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;


    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long walletId) {
        return getWalletById(walletId).getBalance();
    }

    @Override
    @Transactional
    public WalletResponseDto deposit(Long walletId, BigDecimal amount) {

        var wallet = getWalletById(walletId);

        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        return walletMapper.toDto(walletRepository.save(wallet));

    }

    @Override
    @Transactional
    public WalletResponseDto withdraw(Long walletId, BigDecimal amount) {

        var wallet = getWalletById(walletId);

        if(wallet.getBalance().compareTo(amount) < 0)
            throw new InsufficientBalanceException();

        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedAt(LocalDateTime.now());

        return walletMapper.toDto(walletRepository.save(wallet));
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponseDto getWalletByUserId(Long userId) {
        return walletMapper.toDto(walletRepository.findByUserId(userId).orElseThrow(WalletNotFoundException::new));
    }

    //Helper
    private Wallet getWalletById(Long id){
        return walletRepository.findById(id).orElseThrow(WalletNotFoundException::new);
    }
}
