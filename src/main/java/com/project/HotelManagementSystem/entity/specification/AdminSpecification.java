package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.admin.AdminSearchFilter;
import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.constants.AdminType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AdminSpecification {
    public static Specification<Admin> findByName(String name) {
        return (root, query, cb) -> {
            if(name == null || name.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")),"%"+name.toLowerCase().trim()+"%");
        };
    }

    public static Specification<Admin> findByPhone(String phone) {
        return  (root, query, cb) -> {
            if(phone == null || phone.isEmpty()){
                return cb.conjunction();
            }
            return cb.equal(root.get("phone"), phone);
        };
    }

    public static Specification<Admin> findByDateOfBirth(LocalDate dateOfBirth) {
        return (root, query, cb) -> {
            if(dateOfBirth == null){
                return cb.conjunction();
            }
            return cb.equal(root.get("dateOfBirth"), dateOfBirth);
        };
    }

    public static Specification<Admin> findByNationality(String nationality) {
        return  (root, query, cb) -> {
            if(nationality == null || nationality.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("nationality")),"%"+nationality.toLowerCase()+"%");
        };
    }

    public static Specification<Admin> findByPassport(String passportNumber) {
        return (root, query, cb) -> {
            if(passportNumber == null || passportNumber.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("passportNumber")),"%"+passportNumber.toLowerCase()+"%");
        };
    }

    public static Specification<Admin> findByNationalId(String nationalIdNumber) {
        return (root, query, cb) -> {
            if(nationalIdNumber == null || nationalIdNumber.isEmpty()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("nationalIdNumber")),"%"+nationalIdNumber.toLowerCase()+"%");
        };
    }

    public static Specification<Admin> findByAdminType(AdminType adminType) {
        return (root, query, cb) -> {
            if(adminType == null){
                return cb.conjunction();
            }
            return cb.equal(root.get("adminType"), adminType);
        };
    }

    public static Specification<Admin> findByStatusType(StatusType statusType) {
        return (root, query, cb) -> {
            if(statusType == null){
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), statusType);
        };
    }

    public static Specification<Admin> fromFilter(AdminSearchFilter f){
        if(f == null || f.getField() == null) return null;

        return switch (f.getField()){
            case NAME -> stringSpec("name", f);
            case PHONE -> stringSpec("phone", f);
            case NATIONALITY -> stringSpec("nationality", f);
            case PASSPORT_NUMBER -> stringSpec("passportNumber",f);
            case NATIONAL_ID_NUMBER -> stringSpec("nationalIdNumber",f);
            case DATE_OF_BIRTH -> (root, q, cb) -> {
                if(f.getValue() == null || f.getValue().isBlank()) return null;
                String dobString;
                try{
                    dobString = f.getValue().trim();
                }catch (Exception e){
                    return null;
                }
                return cb.equal(root.get("dateOfBirth"),LocalDate.parse(dobString));
            };
            case ADMIN_TYPE -> (root, q, cb) -> {
                if(f.getValue() == null && !f.getValue().isBlank()) return null;
                AdminType adminType;
                try{
                    adminType = AdminType.valueOf(f.getValue());
                }catch (Exception e){
                    return null;
                }
                return cb.equal(root.get("adminType"), adminType);
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


    private static Specification<Admin> stringSpec(String attr, AdminSearchFilter f){
        return (root, q, cb) -> {
            String v = f.getValue();
            if(v == null || v.isBlank()) return null;
            String lv = v.toLowerCase();
            return switch (f.getMatchType()){
                case EXACT -> cb.equal(cb.lower(root.get(attr)), lv);
                case CONTAINS -> cb.like(cb.lower(root.get(attr)), "%"+lv+"%");
                case START_WITH -> cb.like(cb.lower(root.get(attr)), lv+"%");
                case ENDS_WITH ->  cb.like(cb.lower(root.get(attr)), "%"+lv);
            };
        };
    }

}
