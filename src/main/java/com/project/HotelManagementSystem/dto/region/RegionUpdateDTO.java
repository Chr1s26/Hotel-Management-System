package com.project.HotelManagementSystem.dto.region;

import com.project.HotelManagementSystem.entity.Country;
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
public class RegionUpdateDTO {
    private Long id;
    @NotBlank(message = "Region name cannot be empty.")
    @Size(min = 2, max = 100, message = "Region name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "Region name can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String name;
    @NotNull(message = "Country name can't be empty.")
    private Country country;
}
