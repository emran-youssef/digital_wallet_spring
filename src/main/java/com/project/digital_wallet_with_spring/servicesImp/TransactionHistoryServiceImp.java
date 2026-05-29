package com.project.digital_wallet_with_spring.servicesImp;

import com.project.digital_wallet_with_spring.dtos.transactionHistory.TransactionHistoryResponseDto;
import com.project.digital_wallet_with_spring.enums.TransactionType;
import com.project.digital_wallet_with_spring.exceptions.transactionHistory.HistoryNotFoundException;
import com.project.digital_wallet_with_spring.exceptions.wallet.WalletNotFoundException;
import com.project.digital_wallet_with_spring.mappers.TransactionHistoryMapper;
import com.project.digital_wallet_with_spring.repositories.TransactionHistoryRepository;
import com.project.digital_wallet_with_spring.repositories.WalletRepository;
import com.project.digital_wallet_with_spring.services.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImp implements TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionHistoryMapper transactionHistoryMapper;
    private final WalletRepository walletRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponseDto> getHistoryByWalletOwnerId(Long userId) {
        var wallet = walletRepository.findByUserId(userId).orElseThrow(WalletNotFoundException::new);
        return transactionHistoryRepository.findByWalletOrderByArchivedAtDesc(wallet)
                .stream()
                .map(transactionHistoryMapper::toDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponseDto> getHistoryByWalletId(Long walletId) {

        walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);
        var history = transactionHistoryRepository.findByWalletIdOrderByArchivedAtDesc(walletId);

        if(history.isEmpty())
            throw new HistoryNotFoundException();

        return history.stream().map(transactionHistoryMapper::toDto).toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponseDto> getHistoryByWalletIdAndType(Long walletId, TransactionType type) {

        walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);
        var history = transactionHistoryRepository.findByWalletIdAndTypeOrderByArchivedAtDesc(walletId, type);

        if(history.isEmpty())
            throw new HistoryNotFoundException();

        return history.stream().map(transactionHistoryMapper::toDto).toList();

    }
}
