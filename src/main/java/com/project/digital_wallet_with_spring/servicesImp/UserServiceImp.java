package com.project.digital_wallet_with_spring.servicesImp;

import com.project.digital_wallet_with_spring.dtos.jwt.JwtResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.query.PreprocessedQuery;
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
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserResponseDto register(RegisterUserRequest request) {

        log.info("Register attempt for email: {}", request.getEmail());

        if(userRepository.existsByEmail(request.getEmail())) {
            log.info("Register failed - email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistException(); }


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

        log.info("Registered successfully for email:{}", request.getEmail());
        return userMapper.toDto(user);
    }

    @Override
    public JwtResponse login(LoginRequest request){

        log.info("Login attempt for email: {}", request.getEmail());
        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        //generate the token
        var token = jwtService.generateToken(request.getEmail());

        log.info("Login successfully for email: {}", request.getEmail());
        return JwtResponse.builder().token(token).build();


    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        return userMapper.toDto(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        log.debug("Fetching user by id: {}", id);
        return userMapper.toDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

}
