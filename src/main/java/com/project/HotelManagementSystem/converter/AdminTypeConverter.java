package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.AdminType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AdminTypeConverter extends BaseEnumConverter<AdminType,Integer> {
    public AdminTypeConverter() {
        super(AdminType.class);
    }
}
