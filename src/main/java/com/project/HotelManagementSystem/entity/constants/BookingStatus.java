package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BookingStatus implements BaseEnum<Integer>{

    PENDING(1),
    CONFIRMED(2),
    CANCELLED(3),
    REJECTED(4);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
