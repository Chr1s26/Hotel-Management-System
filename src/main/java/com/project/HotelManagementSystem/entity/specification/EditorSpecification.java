package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.entity.constants.EditorType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EditorSpecification {
    public static Specification<Editor> findByName(String name) {
        return (root,query,cb) -> {
            if(name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%");
        };
    }

    public static Specification<Editor> findByPhone(String phone) {
        return (root,query,cb) -> {
            if(phone == null || phone.isEmpty()) {
                return cb.conjunction();
            }
          return cb.like(root.get("phone"),"%" +phone+ "%");
        };
    }

    public static Specification<Editor> findByDOB(LocalDate dateOfBirth) {
        return (root,query,cb) -> {
          if(dateOfBirth == null) {
              return cb.conjunction();
          }
          return cb.equal(root.get("dateOfBirth"), dateOfBirth);
        };
    }

    public static Specification<Editor> findByNationality(String nationality) {
        return (root, query,cb) -> {
            if(nationality == null || nationality.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("nationality")),"%" +nationality.toLowerCase()+ "%");
        };
    }

    public static Specification<Editor> findByPassportNumber(String passportNumber) {
        return (root, query,cb) -> {
            if(passportNumber == null || passportNumber.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("passportNumber")),"%" +passportNumber.toLowerCase()+ "%");
        };
    }

    public static Specification<Editor> findByNationalIdNumber(String nationalIdNumber) {
        return (root, query,cb) -> {
            if(nationalIdNumber == null || nationalIdNumber.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("nationalIdNumber")),"%" +nationalIdNumber.toLowerCase()+ "%");
        };
    }


    public static Specification<Editor> findByStatus(StatusType status) {
        return (root, query,cb) -> {
            if(status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Editor> findByEditorType(EditorType editorType) {
        return (root,query,cb) -> {
            if(editorType == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("editorType"), editorType);
        };
    }
}
