package com.project.HotelManagementSystem.dto.promotion;

import com.project.HotelManagementSystem.converter.DiscountTypeConverter;
import com.project.HotelManagementSystem.entity.constants.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO {
    private Long id;
    private String code;
    private DiscountType discountType;
    private double discountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private int pointAmount;
    private int usageLimit;
    private int timesUsed;
}
