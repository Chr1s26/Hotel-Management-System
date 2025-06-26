package com.project.HotelManagementSystem.dto.promotion;

import com.project.HotelManagementSystem.entity.constants.DiscountType;
import com.project.HotelManagementSystem.validator.NotIntegerString;
import com.project.HotelManagementSystem.validator.NumericString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionCreateDTO {
    private Long id;

    @NotBlank(message = "promotion code cannot be empty")
    @Size(min = 5)
    @NotIntegerString(message = "Promotion code cannot be integer only")
    private String code;

    @NotNull(message = "discount type must be selected")
    private DiscountType discountType;

    @NotNull(message = "discount amount cannot be empty")
    @NumericString(message = "discount amount cannot be string")
    private Double discountAmount;

    @NotNull(message = "start date cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "end date cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "point amount cannot be empty")
    @NumericString(message = "point amount cannot be string")
    private Integer pointAmount;

    @NotNull(message = "usage limit cannot be empty")
    @NumericString(message = "usage limit cannot be string")
    private Integer usageLimit;

    @NotNull(message = "times used cannot be empty")
    @NumericString(message = "times used cannot be string")
    private Integer timesUsed;
}
