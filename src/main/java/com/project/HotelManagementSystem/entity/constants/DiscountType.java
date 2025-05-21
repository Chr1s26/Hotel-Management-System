package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DiscountType implements BaseEnum<Integer> {

    PERCENTAGE(1),
    FIXED_AMOUNT(2);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
