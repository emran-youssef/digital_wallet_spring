package com.project.digital_wallet_with_spring.repositories;


import com.project.digital_wallet_with_spring.entities.TransactionHistory;
import com.project.digital_wallet_with_spring.entities.Wallet;
import com.project.digital_wallet_with_spring.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long>{

    List<TransactionHistory> findByTransactionId(Long transactionId);
    List<TransactionHistory> findByWalletOrderByArchivedAtDesc(Wallet wallet);
    List<TransactionHistory> findByWalletIdAndTypeOrderByArchivedAtDesc(Long walletId, TransactionType type);
    List<TransactionHistory> findByWalletIdOrderByArchivedAtDesc(Long walletId);

}
