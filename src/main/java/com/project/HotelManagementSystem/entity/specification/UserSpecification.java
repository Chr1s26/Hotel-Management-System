package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.user.UserSearchFilter;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> findByName(String name) {
        return (root, query, cb) -> {
            if(name == null || name.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%");
        };
    }

    public static Specification<User> findByEmail(String email) {
        return (root, query, cb) -> {
            if(email == null || email.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase().trim() + "%");
        };
    }

    public static Specification<User> findByStatus(StatusType statusType) {
        return (root, query, cb) -> {
            if(statusType == null){
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), statusType);
        };
    }

    public static Specification<User> fromFilter(UserSearchFilter f){
        if(f == null || f.getField() == null) return null;

        return switch (f.getField()){
                case NAME -> stringSpec("name", f);
                case EMAIL -> stringSpec("email", f);
                case STATUS -> (root, q, cb) -> {
                    if(f.getValue() == null || f.getValue().isBlank()) return null;
                    StatusType statusType;
                    try {
                        statusType = StatusType.valueOf(f.getValue());
                    }catch (Exception e){
                        return null;
                    }
                        return cb.equal(root.get("statusType"), statusType);
                    };
                };
    }


    private static Specification<User> stringSpec(String attr, UserSearchFilter f){
        return (root, q, cb) -> {
            String v = f.getValue();
            String lv = v.toLowerCase();
            if(v == null || v.isBlank()) return null;
            return switch (f.getMatchType()){
                case EXACT -> cb.equal(cb.lower(root.get(attr)), lv);
                case CONTAINS -> cb.like(cb.lower(root.get(attr)), "%"+lv+"%");
                case START_WTITH -> cb.like(cb.lower(root.get(attr)), lv+"%");
                case ENDS_WITH -> cb.like(cb.lower(root.get(attr)), "%"+lv);
            };
        };
    }
}



