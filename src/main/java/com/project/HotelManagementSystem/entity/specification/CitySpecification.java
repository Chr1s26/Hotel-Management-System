package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.entity.City;
import org.springframework.data.jpa.domain.Specification;

public class CitySpecification {

    public static Specification<City> findByName(String name) {
        return (root,query,cb) -> {
            if(name==null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%"+name.toLowerCase()+"%");
        };
    }

    public static Specification<City> findByRegion(String regionName) {
        return (root, query, cb) ->{
            if(regionName==null || regionName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("region").get("name")), "%"+regionName.toLowerCase()+"%");
        };
    }
}
