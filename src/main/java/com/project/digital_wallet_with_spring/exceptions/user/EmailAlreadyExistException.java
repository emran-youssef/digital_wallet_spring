package com.project.digital_wallet_with_spring.exceptions.user;

public class EmailAlreadyExistException extends RuntimeException {

    public EmailAlreadyExistException()
    {
        super("Email already exist!");
    }
}
