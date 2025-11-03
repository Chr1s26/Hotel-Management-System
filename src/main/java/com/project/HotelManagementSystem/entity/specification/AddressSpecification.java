package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.address.AddressSearchFilter;
import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecification {

    public static Specification<Address> fromFilter(AddressSearchFilter f) {
        if (f == null || f.getField() == null) return null;

        return switch (f.getField()) {
            case LATITUDE -> latitudeSpec(f);
            case LONGITUDE -> longitudeSpec(f);
            case ROAD -> stringSpec("road", f);
            case STATUS -> statusSpec(f);
            case ZIPCODE -> stringSpec("zipCode", f);
        };
    }

    private static Specification<Address> statusSpec(AddressSearchFilter f) {
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

    private static Specification<Address> latitudeSpec(AddressSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                double latitude = Double.parseDouble(f.getValue());
                return switch (f.getMatchType()) {
                    case EXACT -> cb.equal(root.get("latitude"), latitude);
                    case CONTAINS, START_WITH, ENDS_WITH -> cb.greaterThanOrEqualTo(root.get("latitude"), latitude);
                };
            } catch (NumberFormatException e) {
                return null;
            }
        };
    }

    private static Specification<Address> longitudeSpec(AddressSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                double longitude = Double.parseDouble(f.getValue());
                return switch (f.getMatchType()) {
                    case EXACT -> cb.equal(root.get("longitude"), longitude);
                    case CONTAINS, START_WITH, ENDS_WITH -> cb.greaterThanOrEqualTo(root.get("longitude"), longitude);
                };
            } catch (NumberFormatException e) {
                return null;
            }
        };
    }

    private static Specification<Address> stringSpec(String attr, AddressSearchFilter f) {
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

}
