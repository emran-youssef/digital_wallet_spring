package com.project.digital_wallet_with_spring.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException()
    {
        super("User not found!");
    }
}
