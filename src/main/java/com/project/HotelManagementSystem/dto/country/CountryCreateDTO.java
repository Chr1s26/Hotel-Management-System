package com.project.HotelManagementSystem.dto.country;

import com.project.HotelManagementSystem.validator.NotIntegerString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryCreateDTO {
    private Long id;
    @NotBlank(message = "Country name cannot be empty.")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "Country name can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String name;
}
