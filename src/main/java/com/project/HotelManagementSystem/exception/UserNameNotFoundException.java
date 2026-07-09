package com.project.HotelManagementSystem.exception;

import lombok.Data;

@Data
public class UserNameNotFoundException extends RuntimeException {

    public UserNameNotFoundException(String message) {
        super(message);
    }
}
