package com.project.HotelManagementSystem.dto.country;

import com.project.HotelManagementSystem.validator.NotIntegerString;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryCreateDTO {
    private Long id;
    @NotBlank(message = "country name cannot be empty")
    @NotIntegerString(message = "country name cannot be an integer")
    private String name;
}
