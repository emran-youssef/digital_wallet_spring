package com.project.digital_wallet_with_spring.dtos.user;

import com.project.digital_wallet_with_spring.entities.Wallet;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Wallet walletId;   //later on wallet -> WalletDto
}
