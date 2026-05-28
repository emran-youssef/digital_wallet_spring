package com.project.digital_wallet_with_spring.servicesImp;

import com.project.digital_wallet_with_spring.dtos.transaction.TransactionResponseDto;
import com.project.digital_wallet_with_spring.dtos.transaction.TransferRequestDto;
import com.project.digital_wallet_with_spring.entities.Transaction;
import com.project.digital_wallet_with_spring.entities.TransactionHistory;
import com.project.digital_wallet_with_spring.enums.TransactionStatus;
import com.project.digital_wallet_with_spring.enums.TransactionType;
import com.project.digital_wallet_with_spring.exceptions.transaction.SameWalletTransferException;
import com.project.digital_wallet_with_spring.exceptions.transaction.TransactionNotFoundException;
import com.project.digital_wallet_with_spring.exceptions.wallet.WalletNotFoundException;
import com.project.digital_wallet_with_spring.mappers.TransactionMapper;
import com.project.digital_wallet_with_spring.repositories.TransactionHistoryRepository;
import com.project.digital_wallet_with_spring.repositories.TransactionRepository;
import com.project.digital_wallet_with_spring.repositories.WalletRepository;
import com.project.digital_wallet_with_spring.services.TransactionService;
import com.project.digital_wallet_with_spring.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {


    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final TransactionHistoryRepository transactionHistoryRepository;


    @Override
    @Transactional
    public TransactionResponseDto transfer(TransferRequestDto request) {

        var sender = walletRepository.findById(request.getSenderWalletId()).orElseThrow(WalletNotFoundException::new);
        var receiver = walletRepository.findById(request.getReceiverWalletId()).orElseThrow(WalletNotFoundException::new);

        if(sender.getId().equals(receiver.getId()))
            throw new SameWalletTransferException();

        walletService.withdraw(sender.getId(), request.getAmount());
        walletService.deposit(receiver.getId(), request.getAmount());

        var transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .sender(sender)
                .receiver(receiver)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        //create history for both sender and receiver:
        var senderHistory = TransactionHistory.builder()
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .wallet(sender)
                .transaction(transaction)
                .archivedAt(LocalDateTime.now())
                .build();

        var receiverHistory = TransactionHistory.builder()
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .wallet(receiver)
                .transaction(transaction)
                .archivedAt(LocalDateTime.now())
                .build();

        transactionHistoryRepository.saveAll(List.of(senderHistory, receiverHistory));

        return transactionMapper.toDto(transaction);
    }


    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDto getTransactionById(Long transactionId) {
        var transaction = transactionRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByWalletId(Long walletId) {
        walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);

        return transactionRepository.findByWalletId(walletId)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }
}
