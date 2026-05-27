package com.project.digital_wallet_with_spring.exceptions.user;

public class EmailAlreadyExist extends RuntimeException {

    public EmailAlreadyExist()
    {
        super("Email already exist!");
    }
}
