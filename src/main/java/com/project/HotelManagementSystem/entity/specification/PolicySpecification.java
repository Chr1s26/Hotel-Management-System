package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.policy.PolicySearchFilter;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class PolicySpecification {

    public static Specification<Policy> fromFilter(PolicySearchFilter f){
        if(f == null || f.getField() == null) return null;

        return switch (f.getField()){
            case TITLE -> stringSpec("title",f);
            case APPLICABLE_TO -> stringSpec("applicableTo",f);
            case STATUS -> statusSpec(f);
        };
    }

    private static Specification<Policy> statusSpec(PolicySearchFilter f){
        return (root,query,cb) -> {
            if(f.getValue() == null || f.getValue().isBlank()) return null;
            StatusType statusType;
            try{
                statusType = StatusType.valueOf(f.getValue());
            }catch (Exception e){
                return null;
            }
            return cb.equal(root.get("status"), statusType);
        };
    }

    private static Specification<Policy> stringSpec(String attr, PolicySearchFilter f){
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
