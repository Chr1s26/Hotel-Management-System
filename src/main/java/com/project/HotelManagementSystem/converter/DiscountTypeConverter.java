package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.DiscountType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DiscountTypeConverter extends BaseEnumConverter<DiscountType,Integer> {
    public DiscountTypeConverter(){
        super(DiscountType.class);
    }
}
