package com.project.HotelManagementSystem.entity.specification;

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
}
