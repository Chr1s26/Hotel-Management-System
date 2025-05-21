package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.CurrencyType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyTypeConverter extends BaseEnumConverter<CurrencyType,Integer>{
    public CurrencyTypeConverter() {
        super(CurrencyType.class);
    }
}
