package com.project.HotelManagementSystem.dto.user;

import com.project.HotelManagementSystem.converter.UserRoleConverter;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.constants.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    @Convert(converter = UserRoleConverter.class)
    private UserRole userRole;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String nationality;
    private int point;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime confirmedAt;
    private Set<Promotion> promotions = new HashSet<>();
}
