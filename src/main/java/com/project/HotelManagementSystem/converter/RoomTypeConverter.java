package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.RoomType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoomTypeConverter extends BaseEnumConverter<RoomType,Integer> {
    public RoomTypeConverter() {
        super(RoomType.class);
    }
}
