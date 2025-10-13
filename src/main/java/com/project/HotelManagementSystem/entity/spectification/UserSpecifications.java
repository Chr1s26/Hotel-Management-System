package com.project.HotelManagementSystem.entity.spectification;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> findByName(String name){
        return (root, query, cb) -> {
            if(name == null || name.isBlank()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() +"%");
        };
    }

    public static Specification<User> findByEmail(String email){
        return (root, query, cb) -> {
            if(email == null || email.isBlank()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase().trim() +"%");
        };
    }

    public static Specification<User> findByStatus(StatusType statusType){
        return (root, query, cb) -> {
            if(statusType == null){
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), statusType);
        };
    }


}
