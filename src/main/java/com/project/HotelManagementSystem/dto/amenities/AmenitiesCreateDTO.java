package com.project.HotelManagementSystem.dto.amenities;

import com.project.HotelManagementSystem.validator.NotIntegerString;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenitiesCreateDTO {
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    @NotIntegerString(message = "Name cannot be an integer")
    private String name;
    @NotBlank(message = "Description cannot be empty")
    @NotIntegerString(message = "Description cannot be an integer")
    private String description;
}
