package com.project.digital_wallet_with_spring.controllers;

import com.project.digital_wallet_with_spring.dtos.wallet.AmountRequestDto;
import com.project.digital_wallet_with_spring.dtos.wallet.WalletResponseDto;
import com.project.digital_wallet_with_spring.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long walletId){
        return ResponseEntity.status(HttpStatus.OK).body(walletService.getBalance(walletId));

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletResponseDto> getWalletByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getWalletByUserId(userId));
    }

    @PostMapping("/deposit/{walletId}")
    public ResponseEntity<WalletResponseDto> deposit(@PathVariable Long walletId, @Valid @RequestBody AmountRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(walletService.deposit(walletId, request.getAmount()));
    }

    @PostMapping("/withdraw/{walletId}")
    public ResponseEntity<WalletResponseDto> withdraw(@PathVariable Long walletId, @Valid @RequestBody AmountRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(walletService.withdraw(walletId, request.getAmount()));
    }


}
