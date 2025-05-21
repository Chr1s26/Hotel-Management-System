package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserRole implements BaseEnum<Integer> {

    ADMIN(1),
    EDITOR(2),
    CUSTOMER(3);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
