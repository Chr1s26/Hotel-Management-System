package com.project.HotelManagementSystem.dto.promotion;

import com.project.HotelManagementSystem.converter.DiscountTypeConverter;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.DiscountType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO {
    private Long id;
    private String code;
    private DiscountType discountType;
    private double discountAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private int pointAmount;
    private int usageLimit;
    private int timesUsed;
    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User createdBy;
    private User updatedBy;
}
