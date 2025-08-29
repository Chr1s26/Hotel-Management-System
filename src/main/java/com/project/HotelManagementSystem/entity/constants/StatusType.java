package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusType implements BaseEnum<Integer>{

    ACTIVE(1),
    DELETED(0);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}

