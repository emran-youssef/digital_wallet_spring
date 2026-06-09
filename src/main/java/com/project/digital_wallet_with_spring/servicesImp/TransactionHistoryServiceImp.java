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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImp implements TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionHistoryMapper transactionHistoryMapper;
    private final WalletRepository walletRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponseDto> getHistoryByWalletOwnerId(Long userId) {
        log.info("Fetching transactions history for userId={}", userId);

        var wallet = walletRepository.findByUserId(userId).orElseThrow(WalletNotFoundException::new);

        var history = transactionHistoryRepository.findByWalletOrderByArchivedAtDesc(wallet)
                .stream().map(transactionHistoryMapper::toDto).toList();

        log.debug("found {} history record for userId={}", history.size(), userId);
        return history;
    }


    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponseDto> getHistoryByWalletId(Long walletId) {
        log.info("Fetching history for a user by walletId={}", walletId);

        walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);
        var history = transactionHistoryRepository.findByWalletIdOrderByArchivedAtDesc(walletId);

        if(history.isEmpty()) {
            log.warn("No transaction history were found for walletId={}", walletId);
            throw new HistoryNotFoundException(); }

        log.debug("Found {} history records for walletId={}", history.size(), walletId);
        return history.stream().map(transactionHistoryMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponseDto> getHistoryByWalletIdAndType(Long walletId, TransactionType type) {
        log.info("Fetching history for a user by walletId={}, and transaction type:{}", walletId, type);

        walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);
        var history = transactionHistoryRepository.findByWalletIdAndTypeOrderByArchivedAtDesc(walletId, type);

        if(history.isEmpty()) {
            log.warn("No transaction history were found for walletId={}, type:{}", walletId, type);
            throw new HistoryNotFoundException(); }

        log.debug("Found {} history records for walletId={} and type:{}", history.size(), walletId, type);
        return history.stream().map(transactionHistoryMapper::toDto).toList();

    }
}
