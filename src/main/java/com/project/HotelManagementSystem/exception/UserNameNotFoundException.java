package com.project.HotelManagementSystem.exception;

import lombok.Data;

@Data
public class UserNameNotFoundException extends RuntimeException {

    private final String view;

    public UserNameNotFoundException(String message, String view) {
        super(message);
        this.view = view;
    }
}
