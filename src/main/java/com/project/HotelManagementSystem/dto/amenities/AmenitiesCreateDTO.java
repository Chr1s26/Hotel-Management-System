package com.project.HotelManagementSystem.dto.amenities;

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
public class AmenitiesCreateDTO {
    private Long id;
    @NotBlank(message = "Amenity name cannot be empty.")
    @Size(min = 2, max = 100, message = "Amenity name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "Amenity name can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String name;
    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "Description can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String description;
}
