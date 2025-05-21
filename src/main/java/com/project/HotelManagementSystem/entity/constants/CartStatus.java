package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CartStatus implements BaseEnum<Integer>{

    ACTIVE(1),
    EXPIRED(2);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
