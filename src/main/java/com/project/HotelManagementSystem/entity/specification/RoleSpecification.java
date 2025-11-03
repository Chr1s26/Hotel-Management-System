package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.role.RoleSearchFilter;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {

    public static Specification<Role> fromFilter(RoleSearchFilter f) {
        if (f == null || f.getField() == null) return null;

        return switch (f.getField()) {
            case ROLE_NAME -> stringSpec("roleName", f);
            case STATUS -> statusSpec(f);
        };
    }

    private static Specification<Role> stringSpec(String attr, RoleSearchFilter f) {
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

    private static Specification<Role> statusSpec(RoleSearchFilter f) {
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
