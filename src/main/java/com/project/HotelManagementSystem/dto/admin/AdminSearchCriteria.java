package com.project.HotelManagementSystem.dto.admin;

import com.project.HotelManagementSystem.entity.constants.AdminType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AdminSearchCriteria {
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String nationalIdNumber;
    private AdminType adminType;
    private StatusType statusType;
    private Integer pageNumber = 0;
    private Integer pageSize = 3;
    private String sortBy = "id";
    private String sortOrder = "asc";
}
