package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AdminType implements BaseEnum<Integer> {
    SUPER_ADMIN(1),
    HOTEL_MANAGER(2),
    CUSTOMER_SUPPORT(3);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
