package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.promotion.PromotionSearchFilter;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.constants.DiscountType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PromotionSpecification {

    public static Specification<Promotion> fromFilter(PromotionSearchFilter f){
        if(f == null || f.getField() == null) return null;

        return switch (f.getField()){
            case CODE -> stringSpec("code", f);
            case DISCOUNT_TYPE -> discountTypeSpec(f);
            case DISCOUNT_AMOUNT -> discountAmountSpec(f);
            case START_DATE -> dateSpec("startDate", f);
            case END_DATE -> dateSpec("endDate", f);
            case STATUS -> statusSpec(f);
        };
    }

    private static Specification<Promotion> statusSpec(PromotionSearchFilter f){
        return (root,query,cb) -> {
            if(f.getValue() == null || f.getValue().isBlank()) return null;
            StatusType statusType;
            try{
                statusType = StatusType.valueOf(f.getValue());
            }catch (Exception e){
                return null;
            }
            return cb.equal(root.get("status"), statusType);
        };
    }


    private static Specification<Promotion> discountAmountSpec(PromotionSearchFilter f) {
        return (root, query, cb) -> {
            if (f == null || f.getValue() == null || f.getValue().isBlank()) {
                return null;
            }
                Double amount = Double.valueOf(f.getValue().trim());
                return cb.equal(root.get("discountAmount"), amount);

        };
    }

    private static Specification<Promotion> discountTypeSpec(PromotionSearchFilter f){
        return (root,query,cb) -> {
            if (f == null || f.getValue() == null) return null;
            DiscountType discountType;
            try{
                  discountType = DiscountType.valueOf(f.getValue());
            }catch (IllegalArgumentException e){
                return null;
            }
                return cb.equal(root.get("discountType"), discountType);

        };
    }

    private static Specification<Promotion> dateSpec(String date, PromotionSearchFilter f){
        return (root, query, cb) -> {
            if(f == null || f.getField() == null) return null;
            String v = f.getValue().trim();
            LocalDate d = parseLocalDate(v);
            if(d == null) return null;
            return cb.equal(root.get(date),d);
        };
    }

    private static LocalDate parseLocalDate(String v) {
        try{
            return LocalDate.parse(v);
        }catch (Exception e){
            return null;
        }
    }

    private static Specification<Promotion> stringSpec(String attr, PromotionSearchFilter f){
        return (root, q, cb) -> {
            String v = f.getValue();
            if(v == null || v.isBlank()) return null;
            String lv = v.toLowerCase();
            return switch (f.getMatchType()){
                case EXACT -> cb.equal(cb.lower(root.get(attr)), lv);
                case CONTAINS -> cb.like(cb.lower(root.get(attr)), "%"+lv+"%");
                case START_WITH -> cb.like(cb.lower(root.get(attr)), lv+"%");
                case ENDS_WITH -> cb.like(cb.lower(root.get(attr)), "%"+lv);
            };
        };
    }
}
