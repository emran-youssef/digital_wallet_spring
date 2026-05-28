package com.project.digital_wallet_with_spring.dtos.transaction;

import com.project.digital_wallet_with_spring.enums.TransactionStatus;
import com.project.digital_wallet_with_spring.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {

    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private Long senderWalletId;
    private Long receiverWalletId;

}
