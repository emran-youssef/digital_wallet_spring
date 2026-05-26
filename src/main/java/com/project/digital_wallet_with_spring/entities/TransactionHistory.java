package com.project.digital_wallet_with_spring.entities;

import com.project.digital_wallet_with_spring.enums.TransactionStatus;
import com.project.digital_wallet_with_spring.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name ="type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name ="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "archived_at", nullable = false)
    private LocalDateTime archivedAt;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

}
