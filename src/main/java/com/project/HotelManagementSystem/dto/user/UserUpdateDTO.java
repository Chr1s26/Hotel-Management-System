package com.project.HotelManagementSystem.dto.user;

import com.project.HotelManagementSystem.validator.NumericString;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private Long id;
    @NotBlank(message = "Name cannot be empty.")
    private String name;
    @NotBlank(message = "Email cannot be empty.")
    private String email;
    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 3 , message = "Password must include at least 3 characters.")
    private String password;
    //    @NotBlank(message = "Phone number cannot be empty.")
//    @Size(min = 8, message = "Phone number must include at least 8 characters.")
//    @NumericString(message = "Phone number cannot be string")
//    private String phone;
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @NotNull(message = "Date of Birth cannot be empty.")
//    private LocalDate dateOfBirth;
//    @NotBlank(message = "Nationality cannot be empty.")
//    private String nationality;
//    private int point;
//    private Set<Long> rolesId = new HashSet<>();
//    private MultipartFile file;
}
