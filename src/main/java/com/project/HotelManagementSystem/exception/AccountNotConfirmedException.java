package com.project.HotelManagementSystem.exception;

public class AccountNotConfirmedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public AccountNotConfirmedException(String message) {
        super(message);
    }
}
