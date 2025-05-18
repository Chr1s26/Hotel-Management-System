package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentStatus {

    PENDING(1),
    PAID(2),
    FAILED(3),
    REFUNDED(4),
    PARTIALLY_PAID(5);

    private final int value;

}
