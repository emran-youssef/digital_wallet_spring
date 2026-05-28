package com.project.digital_wallet_with_spring.mappers;

import com.project.digital_wallet_with_spring.dtos.transaction.TransactionResponseDto;
import com.project.digital_wallet_with_spring.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "sender.id", target = "senderWalletId")
    @Mapping(source = "receiver.id", target = "receiverWalletId")
    TransactionResponseDto toDto(Transaction transaction);
}
