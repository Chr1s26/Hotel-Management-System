package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.editor.EditorSearchFilter;
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

    public static Specification<Editor> fromFilter(EditorSearchFilter f) {
        if(f == null || f.getField() == null) {return null;}
        return switch (f.getField()){
            case NAME -> stringSpec("name",f);
            case PHONE -> stringSpec("phone",f);
            case DATE_OF_BIRTH -> (root,q,cb) -> {
                if(f.getValue() == null || f.getValue().isBlank()) {return null;}
                String v = f.getValue().trim();
                LocalDate d = parseLocalDate(v);
                if(d == null) return null;
                return cb.equal(root.get("dateOfBirth"), d);

            };
            case NATIONALITY -> stringSpec("nationality",f);
            case NATIONAL_ID_NUMBER -> stringSpec("nationalIdNumber",f);
            case PASSPORT_NUMBER -> stringSpec("passportNumber",f);
            case EDITOR_TYPE -> (root,q,cb) -> {
                if(f.getValue() == null || f.getValue().isBlank()) return null;
                EditorType editorType;
                try{
                    editorType = EditorType.valueOf(f.getValue());
                }catch (Exception e){
                    return null;
                }
                return cb.equal(root.get("editorType"), editorType);
            };

            case STATUS -> (root,q,cb) -> {
              if(f.getValue() == null || f.getValue().isBlank()) return null;
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

    private static LocalDate parseLocalDate(String v) {
        try{
            return LocalDate.parse(v);
        }catch (Exception e){
            return null;
        }
    }

    private static Specification<Editor> stringSpec(String attr,EditorSearchFilter f){
        return (root,q,cb) -> {
            String v = f.getValue();
            if(v == null || v.isBlank()) return  null;
            String lv = v.toLowerCase();
            return switch (f.getMatchType()){
                case EXACT -> cb.equal(cb.lower(root.get(attr)), lv);
                case CONTAINS -> cb.like(cb.lower(root.get(attr)), "%"+lv.toLowerCase()+"%");
                case START_WITH -> cb.like(cb.lower(root.get(attr)), lv.toLowerCase()+"%");
                case ENDS_WITH -> cb.like(cb.lower(root.get(attr)), "%"+lv.toLowerCase());
            };
        };
    }
}
