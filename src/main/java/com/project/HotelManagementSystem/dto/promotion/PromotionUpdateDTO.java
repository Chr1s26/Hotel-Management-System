package com.project.HotelManagementSystem.dto.promotion;

import com.project.HotelManagementSystem.entity.constants.DiscountType;
import com.project.HotelManagementSystem.validator.NotIntegerString;
import com.project.HotelManagementSystem.validator.NumericString;
import com.project.HotelManagementSystem.validator.ValidDateRange;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidDateRange
public class PromotionUpdateDTO {
    private Long id;
    @NotBlank(message = "Promotion code cannot be empty.")
    @Size(min = 3, max = 50, message = "Promotion code must be between 3 and 50 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\-_.]+$", message = "Promotion code can only contain letters, numbers, dashes (-), underscores (_), and dots (.)")
    private String code;
    @NotNull(message = "discount type must be selected")
    private DiscountType discountType;
    @NotNull(message = "discount amount cannot be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount amount must be greater than 0.")
    private Double discountAmount;
    @NotNull(message = "Start date cannot be empty.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull(message = "End date cannot be empty.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotNull(message = "Point amount cannot be empty.")
    @Min(value = 0, message = "Point amount cannot be negative.")
    private Integer pointAmount;
    @NotNull(message = "Usage limit cannot be empty.")
    @Min(value = 1, message = "Usage limit must be at least 1.")
    private Integer usageLimit;
    @NotNull(message = "times used cannot be empty")
    @Min(value = 0, message = "Times used cannot be negative.")
    private Integer timesUsed;
}
