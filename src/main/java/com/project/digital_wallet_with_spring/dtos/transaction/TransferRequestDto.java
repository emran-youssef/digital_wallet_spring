package com.project.digital_wallet_with_spring.dtos.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {

    @NotNull
    private Long senderWalletId;

    @NotNull
    private Long receiverWalletId;

    @NotNull
    @DecimalMin(value = "1.00", message = "amount must be greater than or equal to 1.00$")
    private BigDecimal amount;

}
