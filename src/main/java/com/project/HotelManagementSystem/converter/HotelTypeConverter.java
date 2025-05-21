package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.HotelType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class HotelTypeConverter extends BaseEnumConverter<HotelType,Integer> {
    public HotelTypeConverter() {
        super(HotelType.class);
    }
}
