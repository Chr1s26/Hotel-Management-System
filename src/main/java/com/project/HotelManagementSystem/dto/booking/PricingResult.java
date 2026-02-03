package com.project.HotelManagementSystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingResult {
    private double originalPrice;
    private double discountedPrice;
    private String discountLabel;
    private boolean hasPromotion;
}
