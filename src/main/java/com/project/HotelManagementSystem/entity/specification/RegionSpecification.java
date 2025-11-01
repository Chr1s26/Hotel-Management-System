package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.region.RegionSearchFilter;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
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

    public static Specification<User> findByStatus(StatusType statusType) {
        return (root, query, cb) -> {
            if(statusType == null){
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), statusType);
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

    public static Specification<Region> fromFilter(RegionSearchFilter f) {
        if(f == null || f.getField() == null) return null;

        return switch (f.getField()){
            case NAME -> stringSpec("name",f);
            case STATUS -> (root,q,cb)->{
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

    private static Specification<Region> stringSpec(String attr, RegionSearchFilter f) {
        return (root,q,cb) -> {
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
