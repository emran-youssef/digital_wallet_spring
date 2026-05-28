package com.project.digital_wallet_with_spring.dtos.wallet;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletResponseDto {

    private Long id;
    private BigDecimal balance;
    private LocalDateTime updatedAt;
    private Long userId;
}
