package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {

    ADMIN(1),
    EDITOR(2),
    CUSTOMER(3);

    private final int value;

}
