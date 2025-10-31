package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.city.CitySearchFilter;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.constants.StatusType;
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

    public static Specification<City> fromFilter(CitySearchFilter f){
        if(f == null || f.getField() == null) return null;

        return switch (f.getField()){
            case NAME -> stringSpec("name", f);
            case STATUS -> (root, q, cb) -> {
                if(f.getValue() == null || f.getValue().isBlank()) return null;
                StatusType statusType;
                try {
                    statusType = StatusType.valueOf(f.getValue());
                }catch (Exception e){
                    return null;
                }
                return cb.equal(root.get("status"), statusType);
            };
        };
    }

    private static Specification<City> stringSpec(String attr, CitySearchFilter f){
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
