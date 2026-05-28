package com.project.digital_wallet_with_spring.mappers;

import com.project.digital_wallet_with_spring.dtos.wallet.WalletResponseDto;
import com.project.digital_wallet_with_spring.entities.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(source = "user.id", target = "userId")
    WalletResponseDto toDto(Wallet wallet);
}
