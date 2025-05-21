package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.CartStatus;
import jakarta.persistence.Converter;

@Converter(autoApply=true)
public class CartStatusConverter extends BaseEnumConverter<CartStatus,Integer> {
    public CartStatusConverter() {
        super(CartStatus.class);
    }
}
