package com.project.digital_wallet_with_spring.controllers;

import com.project.digital_wallet_with_spring.dtos.transaction.TransactionResponseDto;
import com.project.digital_wallet_with_spring.dtos.transaction.TransferRequestDto;
import com.project.digital_wallet_with_spring.dtos.wallet.AmountRequestDto;
import com.project.digital_wallet_with_spring.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit/{walletId}")
    public ResponseEntity<TransactionResponseDto> deposit(@PathVariable Long walletId, @Valid @RequestBody AmountRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.deposit(walletId, request.getAmount()));
    }

    @PostMapping("/withdraw/{walletId}")
    public ResponseEntity<TransactionResponseDto> withdraw(@PathVariable Long walletId, @Valid @RequestBody AmountRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.withdraw(walletId, request.getAmount()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transfer(@Valid @RequestBody TransferRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.transfer(request));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long transactionId){
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionById(transactionId));
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionByWalletId(@PathVariable Long walletId){
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionsByWalletId(walletId));

    }


}
