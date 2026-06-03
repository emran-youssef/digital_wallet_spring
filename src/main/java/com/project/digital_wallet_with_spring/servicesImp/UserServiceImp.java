package com.project.digital_wallet_with_spring.servicesImp;

import com.project.digital_wallet_with_spring.dtos.user.LoginRequest;
import com.project.digital_wallet_with_spring.dtos.user.RegisterUserRequest;
import com.project.digital_wallet_with_spring.dtos.user.UserResponseDto;
import com.project.digital_wallet_with_spring.entities.User;
import com.project.digital_wallet_with_spring.entities.Wallet;
import com.project.digital_wallet_with_spring.exceptions.user.EmailAlreadyExistException;
import com.project.digital_wallet_with_spring.exceptions.user.UserNotFoundException;
import com.project.digital_wallet_with_spring.mappers.UserMapper;
import com.project.digital_wallet_with_spring.repositories.UserRepository;
import com.project.digital_wallet_with_spring.repositories.WalletRepository;
import com.project.digital_wallet_with_spring.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserResponseDto register(RegisterUserRequest request) {

        if(userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistException();

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) //  hash the password
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        var wallet = Wallet.builder()
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        var savedWallet = walletRepository.save(wallet);
        user.setWallet(savedWallet);

        return userMapper.toDto(user);

    }

    @Override
    public UserResponseDto login(LoginRequest request){
        authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);

    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

}
