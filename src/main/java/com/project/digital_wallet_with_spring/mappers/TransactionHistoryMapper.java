package com.project.digital_wallet_with_spring.mappers;

import com.project.digital_wallet_with_spring.dtos.transactionHistory.TransactionHistoryResponseDto;
import com.project.digital_wallet_with_spring.entities.TransactionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionHistoryMapper {

    @Mapping(source = "wallet.id", target = "walletId")
    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(target = "direction", expression = "java(resolveDirection(history))")
    @Mapping(target = "receiverId", expression = "java(resolveReceiverId(history))")
    TransactionHistoryResponseDto toDto(TransactionHistory history);


    // to map the field direction in the dto
    default String resolveDirection(TransactionHistory history){
        Long walletId = history.getWallet().getId();

        if(history.getTransaction().getSender() != null &&
                history.getTransaction().getSender().getId().equals(walletId))
            return "SENT";

        if(history.getTransaction().getReceiver() != null &&
                history.getTransaction().getReceiver().getId().equals(walletId))
            return "RECEIVED";

        return "UNKNOWN";
    }

    default Long resolveReceiverId(TransactionHistory history){

        // DEPOSIT & TRANSFER: receiver exists, return receiver's wallet id
        if (history.getTransaction().getReceiver() != null)
            return history.getTransaction().getReceiver().getId();

        // WITHDRAW: no receiver, return sender's wallet id (the user's own wallet)
        return history.getTransaction().getSender().getId();

    }
}
