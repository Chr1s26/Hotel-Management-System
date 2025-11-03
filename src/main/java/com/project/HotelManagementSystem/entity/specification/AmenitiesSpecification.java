package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.amenities.AmenitiesSearchFilter;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class AmenitiesSpecification {

    public static Specification<Amenities> fromFilter(AmenitiesSearchFilter f){
        if (f == null || f.getField() == null) return null;
        return switch (f.getField()) {
            case NAME -> stringSpec("name", f);
            case STATUS -> statusSpec(f);
        };
    }

    private static Specification<Amenities> stringSpec(String attr, AmenitiesSearchFilter f) {
        return (root, q, cb) -> {
            String v = f.getValue();
            if (v == null || v.isBlank()) return null;
            String lv = v.toLowerCase();
            return switch (f.getMatchType()) {
                case EXACT -> cb.equal(cb.lower(root.get(attr)), lv);
                case CONTAINS -> cb.like(cb.lower(root.get(attr)), "%" + lv + "%");
                case START_WITH -> cb.like(cb.lower(root.get(attr)), lv + "%");
                case ENDS_WITH -> cb.like(cb.lower(root.get(attr)), "%" + lv);
            };
        };
    }

    private static Specification<Amenities> statusSpec(AmenitiesSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                StatusType status = StatusType.valueOf(f.getValue().toUpperCase());
                return cb.equal(root.get("status"), status);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

}
