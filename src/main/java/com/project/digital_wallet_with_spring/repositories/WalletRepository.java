package com.project.digital_wallet_with_spring.repositories;

import com.project.digital_wallet_with_spring.entities.User;
import com.project.digital_wallet_with_spring.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);
    Optional<Wallet> findByUserId(Long id);
    Optional<Wallet> findByUser_Username(String username);
    Optional<Wallet> findByUser_Email(String email);


}
