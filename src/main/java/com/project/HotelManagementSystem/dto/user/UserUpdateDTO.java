package com.project.HotelManagementSystem.dto.user;

import com.project.HotelManagementSystem.converter.UserRoleConverter;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.constants.UserRole;
import com.project.HotelManagementSystem.validator.NumericString;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Size(min = 5 , message = "Password must include at least 5 characters.")
    private String password;
    @NotBlank(message = "Phone number cannot be empty.")
    @Size(min = 8, message = "Phone number must include at least 8 characters.")
    @NumericString(message = "Phone number cannot be string")
    private String phone;
    @Convert(converter = UserRoleConverter.class)
    private UserRole userRole;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of Birth cannot be empty.")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Nationality cannot be empty.")
    private String nationality;
    private int point;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime confirmedAt;
    private Set<Long> promotionIds = new HashSet<>();
}
