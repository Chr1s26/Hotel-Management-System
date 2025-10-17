package com.project.HotelManagementSystem.dto.editor;

import com.project.HotelManagementSystem.entity.constants.EditorType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EditorSeearchCriteria {
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String nationalIdNumber;
    private EditorType editorType;
    private StatusType status;

    private Integer pageNumber = 0;
    private Integer pageSize = 3;
    private String sortBy = "id";
    private String sortOrder = "asc";
}
