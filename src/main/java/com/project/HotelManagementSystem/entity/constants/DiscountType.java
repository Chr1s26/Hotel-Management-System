package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DiscountType {

    PERCENTAGE(1),
    FIXED_AMOUNT(2);

    private final int value;

}
