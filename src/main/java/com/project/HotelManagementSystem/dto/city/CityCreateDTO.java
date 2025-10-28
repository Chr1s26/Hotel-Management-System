package com.project.HotelManagementSystem.dto.city;

import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.validator.NotIntegerString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityCreateDTO {
    private Long id;
    @NotBlank(message = "City name cannot be empty.")
    @Size(min = 2, max = 100, message = "City name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "City name can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String name;
    @NotNull(message = "Region must be selected.")
    private Region region;
}
