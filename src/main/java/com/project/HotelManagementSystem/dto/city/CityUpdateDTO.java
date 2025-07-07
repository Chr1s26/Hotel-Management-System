package com.project.HotelManagementSystem.dto.city;

import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.validator.NotIntegerString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityUpdateDTO {
    private Long id;
    @NotBlank(message = "name cannot be empty")
    @NotIntegerString(message = "city name cannot be an integer")
    private String name;
    @NotNull(message = "region cannot be empty")
    private Region region;
}
