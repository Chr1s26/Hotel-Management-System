package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.StatusType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusTypeConverter extends BaseEnumConverter<StatusType, Integer> {
    public StatusTypeConverter() {
        super(StatusType.class);
    }
}
