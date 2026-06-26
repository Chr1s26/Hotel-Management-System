package com.project.HotelManagementSystem.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountNotConfirmedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public AccountNotConfirmedException(String message) {
        super(message);
    }
}
