package com.project.HotelManagementSystem.dto.customer;

import com.project.HotelManagementSystem.validator.admin.AvailableAdminCreateUser;
import com.project.HotelManagementSystem.validator.customer.AvailableCustomerCreateUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreateDTO {
    private Long id;
    @NotBlank(message = "Name cannot be empty.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name cannot include numbers or symbols")
    private String name;
    @NotBlank(message = "Phone number cannot be empty.")
    @Pattern(regexp = "^[0-9\\-\\s()]{8,15}$", message = "Invalid phone number format")
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of Birth cannot be empty.")
    @PastOrPresent(message = "Birth date cannot be in the future")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Nationality cannot be empty.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Nationality cannot include numbers or symbols")
    private String nationality;
    private boolean vipStatus;
    @AvailableCustomerCreateUser(message = "This user is already assigned to another admin.")
    private Long user;
}
