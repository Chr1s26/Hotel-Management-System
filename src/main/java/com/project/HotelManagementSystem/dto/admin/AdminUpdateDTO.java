package com.project.HotelManagementSystem.dto.admin;

import com.project.HotelManagementSystem.entity.constants.AdminType;
import com.project.HotelManagementSystem.validator.AvailableAdminEditUser;
import com.project.HotelManagementSystem.validator.NumericString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateDTO {
    private Long id;
    @NotBlank(message = "Name cannot be empty.")
    private String name;
    @NotBlank(message = "Phone number cannot be empty.")
    @Size(min = 8, message = "Phone number must include at least 8 characters.")
    @NumericString(message = "Phone number cannot be string")
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of Birth cannot be empty.")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Nationality cannot be empty.")
    private String nationality;
    private String passportNumber;
    private String nationalIdNumber;
    @NotNull(message = "Admin type cannot be empty")
    private AdminType adminType;
    @AvailableAdminEditUser(message = "This user is already assigned to another admin.")
    private Long user;
}
