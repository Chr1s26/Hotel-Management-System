package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchFilter;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.constants.HotelType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class HotelSpecification {

    public static Specification<Hotel> fromFilter(HotelSearchFilter f) {
        if (f == null || f.getField() == null) return null;

        return switch (f.getField()) {
            case NAME -> stringSpec("name", f);
            case PHONE -> stringSpec("phoneNumber", f);
            case EMAIL -> stringSpec("email", f);
            case STATUS -> statusSpec(f);
            case RATING -> ratingSpec(f);
            case HOTEL_TYPE -> hotelTypeSpec(f);
        };
    }

    private static Specification<Hotel> stringSpec(String attr, HotelSearchFilter f) {
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

    private static Specification<Hotel> statusSpec(HotelSearchFilter f) {
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

    private static Specification<Hotel> ratingSpec(HotelSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                double rating = Double.parseDouble(f.getValue());
                return switch (f.getMatchType()) {
                    case EXACT -> cb.equal(root.get("rating"), rating);
                    case CONTAINS, START_WITH, ENDS_WITH -> cb.greaterThanOrEqualTo(root.get("rating"), rating);
                };
            } catch (NumberFormatException e) {
                return null;
            }
        };
    }

    private static Specification<Hotel> hotelTypeSpec(HotelSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                HotelType type = HotelType.valueOf(f.getValue().toUpperCase());
                return cb.equal(root.get("hotelType"), type);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }
}
