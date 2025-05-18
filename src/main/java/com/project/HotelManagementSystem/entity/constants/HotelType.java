package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HotelType {

    APARTMENT(1),
    VILLA(2),
    CONDO(3),
    STUDIO(4),
    BUNGALOW(5),
    COTTAGE(6),
    PENTHOUSE(7),
    LOFT(8),
    CABIN(9),
    GLAMPING_TENT(10);

    private final int value;

}
