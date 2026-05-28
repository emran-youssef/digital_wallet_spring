package com.project.digital_wallet_with_spring.repositories;

import com.project.digital_wallet_with_spring.entities.Transaction;
import com.project.digital_wallet_with_spring.entities.Wallet;
import com.project.digital_wallet_with_spring.enums.TransactionStatus;
import com.project.digital_wallet_with_spring.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySender(Wallet sender);
    List<Transaction> findByReceiver(Wallet receiver);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.sender.id = :walletId OR t.receiver.id = :walletId
            ORDER BY t.createdAt DESC
            """)
    List<Transaction> findByWalletId(@Param("walletId") Long walletId);

    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findByType(TransactionType type);

    @Query("""
            SELECT SUM(t.amount)
            FROM Transaction t
            WHERE t.sender.id = :walletId
            """)
    BigDecimal getTotalSentAmount(@Param("walletId") Long walletId);


}
