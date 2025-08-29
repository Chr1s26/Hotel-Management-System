package com.project.HotelManagementSystem.dto.admin;

import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.AdminType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateDTO {
    private Long id;
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String nationalIdNumber;
    private AdminType adminType;
    private Long app_user_id;
    private MultipartFile file;
}
