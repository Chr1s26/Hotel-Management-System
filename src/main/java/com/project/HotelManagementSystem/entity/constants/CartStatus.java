package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CartStatus {

    ACTIVE(1),
    EXPIRED(2);

    private final int value;

}
