package com.project.HotelManagementSystem.dto.user;

import com.project.HotelManagementSystem.validator.NumericString;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name cannot include numbers or symbols")
    private String name;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 3 , message = "Password must include at least 3 characters.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/])(?!.*\\s).{8,}$",
            message = "Password must contain upper, lower, digit, and symbol, and no spaces"
    )
    private String password;
}
