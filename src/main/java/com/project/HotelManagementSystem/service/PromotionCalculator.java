package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.constants.DiscountType;
import com.project.HotelManagementSystem.dto.booking.PricingResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class PromotionCalculator {
    public PricingResult applyPromotion(double basePrice, Set<Promotion> roomPromotions, Set<Promotion> hotelPromotions, LocalDate checkIn) {
        Promotion promo = findBestPromotion(roomPromotions, checkIn);
        if (promo == null) {
            promo = findBestPromotion(hotelPromotions, checkIn);
        }
        if (promo == null) {
            return new PricingResult(basePrice, basePrice, null, false);
        }
        double discounted = basePrice;

        if (promo.getDiscountType() == DiscountType.PERCENTAGE) {
            discounted = basePrice * (1 - promo.getDiscountAmount() / 100);
        } else {
            discounted = Math.max(0, basePrice - promo.getDiscountAmount());
        }
        String label = promo.getDiscountType() == DiscountType.PERCENTAGE
                ? promo.getDiscountAmount() + "% OFF"
                : "$" + promo.getDiscountAmount() + " OFF";

        return new PricingResult(basePrice, discounted, label, true);
    }

    private Promotion findBestPromotion(Set<Promotion> promotions, LocalDate date) {
        if (promotions == null) return null;

        return promotions.stream()
                .filter(p ->
                        !date.isBefore(p.getStartDate()) &&
                                !date.isAfter(p.getEndDate()) &&
                                (p.getTimesUsed() < p.getUsageLimit())
                )
                .findFirst()
                .orElse(null);
    }
}
