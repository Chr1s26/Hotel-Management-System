package com.project.HotelManagementSystem.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private Long id;
    @NotBlank(message = "Name cannot be empty.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name cannot include numbers or symbols")
    private String name;
    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 3 , message = "Password must include at least 3 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/])(?!.*\\s).{8,}$", message = "Password must contain upper, lower, digit, and symbol, and no spaces")
    private String password;
}
