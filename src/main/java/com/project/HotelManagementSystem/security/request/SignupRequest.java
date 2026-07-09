package com.project.HotelManagementSystem.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name cannot include numbers or symbols")
    private String name;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 50)
    private String email;
    @NotBlank
    @Size(min = 3, max = 60)
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/])(?!.*\\s).{8,}$", message = "Password must contain upper, lower, digit, and symbol, and no spaces")
    private String password;
}
