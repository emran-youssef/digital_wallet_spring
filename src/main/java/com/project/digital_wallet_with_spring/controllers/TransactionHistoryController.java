package com.project.digital_wallet_with_spring.controllers;

import com.project.digital_wallet_with_spring.dtos.transactionHistory.TransactionHistoryResponseDto;
import com.project.digital_wallet_with_spring.enums.TransactionType;
import com.project.digital_wallet_with_spring.services.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionHistoryResponseDto>> gitHistoryByEmail(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(transactionHistoryService.getHistoryByWalletOwnerId(userId));
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionHistoryResponseDto>> getByWalletId(@PathVariable Long walletId){
        return ResponseEntity.status(HttpStatus.OK).body(transactionHistoryService.getHistoryByWalletId(walletId));
    }

    @GetMapping("/wallet/{walletId}/type/{type}")
    public ResponseEntity<List<TransactionHistoryResponseDto>> getHistoryByWalletIdAndType
            (@PathVariable Long walletId, @PathVariable TransactionType type){
        return ResponseEntity.status(HttpStatus.OK).body(transactionHistoryService.getHistoryByWalletIdAndType(walletId, type));

    }


}
