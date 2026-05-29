package com.project.digital_wallet_with_spring.dtos.transactionHistory;

import com.project.digital_wallet_with_spring.enums.TransactionStatus;
import com.project.digital_wallet_with_spring.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionHistoryResponseDto {

    private Long id;
    private LocalDateTime archivedAt;
    private BigDecimal amount;
    private TransactionType type;
    private Long receiverId;
    private TransactionStatus status;
    private Long walletId;
    private Long transactionId;
    private String direction;

}
