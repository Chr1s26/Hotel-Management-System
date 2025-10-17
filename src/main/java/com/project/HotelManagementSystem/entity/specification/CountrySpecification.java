package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.entity.Country;
import org.springframework.data.jpa.domain.Specification;

public class CountrySpecification {
    public static Specification<Country> findByName(String name) {
        return (root,query,cb) -> {
            if(name==null || name.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%"+name.toLowerCase()+"%");
        };
    }
}
