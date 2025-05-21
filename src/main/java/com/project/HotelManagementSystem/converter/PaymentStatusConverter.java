package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.PaymentStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusConverter extends BaseEnumConverter<PaymentStatus,Integer> {
    public PaymentStatusConverter() {
        super(PaymentStatus.class);
    }
}
