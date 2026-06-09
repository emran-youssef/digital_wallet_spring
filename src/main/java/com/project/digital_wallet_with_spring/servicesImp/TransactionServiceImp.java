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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
    public TransactionResponseDto deposit(Long walletId, BigDecimal amount) {
        log.info("Deposit transaction started. walletId={}, amount={}", walletId, amount);

        var wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);

        walletService.deposit(walletId, amount);

        var transaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .receiver(wallet)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        var history = TransactionHistory.builder()
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .wallet(wallet)
                .transaction(transaction)
                .archivedAt(LocalDateTime.now())
                .build();

        transactionHistoryRepository.save(history);

        log.info("Deposit transaction completed, transactionId={}, walletId={}", transaction.getId(), walletId);
        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDto transfer(TransferRequestDto request) {
        log.info("Transfer started from sender:{} to receiver:{}", request.getSenderWalletId(), request.getReceiverWalletId());

        var sender = walletRepository.findById(request.getSenderWalletId()).orElseThrow(WalletNotFoundException::new);
        var receiver = walletRepository.findById(request.getReceiverWalletId()).orElseThrow(WalletNotFoundException::new);

        if(sender.getId().equals(receiver.getId())) {
            log.warn("Transfer rejected, same wallet, walletId:{}", sender.getId());
            throw new SameWalletTransferException(); }

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

        log.info("Transfer Completed, transactionId:{}, senderWalletId:{}, receiverWalletId:{}",
                transaction.getId(), sender.getId(), receiver.getId());
        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDto withdraw(Long walletId, BigDecimal amount) {
        log.info("Withdrawal transaction started. walletId={}, amount={}", walletId, amount);

        var wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);

        walletService.withdraw(walletId, amount);

        var transaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .status(TransactionStatus.COMPLETED)
                .sender(wallet)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        var history = TransactionHistory.builder()
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .status(TransactionStatus.COMPLETED)
                .wallet(wallet)
                .transaction(transaction)
                .archivedAt(LocalDateTime.now())
                .build();

        transactionHistoryRepository.save(history);

        log.info("Withdrawal transaction completed. transactionId={}, walletId={}",
                transaction.getId(), walletId);
        return transactionMapper.toDto(transaction);
    }


    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDto getTransactionById(Long transactionId) {

        log.debug("Fetching transaction. transactionId={}", transactionId);
        return transactionMapper.toDto(transactionRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByWalletId(Long walletId) {

        log.info("Fetching all transactions for walletId={}", walletId);
        walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);
        return transactionRepository.findByWalletId(walletId)
                .stream().map(transactionMapper::toDto).toList();
    }
}
