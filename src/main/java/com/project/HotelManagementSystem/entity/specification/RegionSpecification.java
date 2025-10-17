package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.entity.Region;
import org.springframework.data.jpa.domain.Specification;

public class RegionSpecification {

    public static Specification<Region> findByName(String name) {
        return (root,query,cb) -> {
            if(name==null || name.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")),"%"+name.toLowerCase()+"%");
        };
    }

    public static Specification<Region> findByCountry(String countryName) {
        return (root, q, cb) -> {
            if (countryName == null || countryName.isBlank()) return cb.conjunction();
            return cb.like(
                    cb.lower(root.get("country").get("name")),
                    "%" + countryName.toLowerCase()+ "%"
            );
        };
    }

}
