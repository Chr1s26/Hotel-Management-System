package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingStatus {

    PENDING(1),
    CONFIRMED(2),
    CANCELLED(3),
    REJECTED(4);

    private final int value;

}
