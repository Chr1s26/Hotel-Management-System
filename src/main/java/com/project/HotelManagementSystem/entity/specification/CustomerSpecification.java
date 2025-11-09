package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.customer.CustomerSearchFilter;
import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CustomerSpecification {

    public static Specification<Customer> fromFilter(CustomerSearchFilter f){
        if(f == null || f.getField() == null)return null;

        return switch (f.getField()){
            case NAME -> stringSpec("name",f);
            case PHONE -> stringSpec("phone",f);
            case NATIONALITY -> stringSpec("nationality",f);
            case DATE_OF_BIRTH -> (root, q, cb) -> {
                if(f.getValue() == null || f.getValue().isBlank()) return null;
                String dobString;
                try{
                    dobString = f.getValue().trim();
                }catch (Exception e){
                    return null;
                }
                return cb.equal(root.get("dateOfBirth"), LocalDate.parse(dobString));
            };
            case VIP_STATUS -> (root, q, cb) -> {
                if (f.getValue() == null || f.getValue().isBlank()) return null;

                Boolean vip;
                try {
                    String v = f.getValue().trim().toLowerCase();
                    vip = switch (v) {
                        case "true", "yes", "1" -> true;
                        case "false", "no", "0" -> false;
                        default -> null;
                    };
                } catch (Exception e) {
                    return null;
                }

                return vip == null ? null : cb.equal(root.get("vipStatus"), vip);
            };
            case STATUS -> (root, q, cb) -> {
                if(f.getValue() == null && f.getValue().isBlank())return null;
                StatusType statusType;
                try{
                    statusType = StatusType.valueOf(f.getValue());
                }catch (Exception e){
                    return null;
                }
                return cb.equal(root.get("status"), statusType);
            };
        };
    }

    private static Specification<Customer> stringSpec(String attr, CustomerSearchFilter f) {
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
