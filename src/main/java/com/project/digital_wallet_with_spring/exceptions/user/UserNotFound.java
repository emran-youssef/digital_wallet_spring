package com.project.digital_wallet_with_spring.exceptions.user;

public class UserNotFound extends RuntimeException {
    public UserNotFound()
    {
        super("User not found!");
    }
}
