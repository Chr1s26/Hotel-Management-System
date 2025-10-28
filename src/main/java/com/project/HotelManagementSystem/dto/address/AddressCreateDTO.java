package com.project.HotelManagementSystem.dto.address;

import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.validator.NumericString;
import com.project.HotelManagementSystem.validator.ValidCoordinates;
import com.project.HotelManagementSystem.validator.NotIntegerString;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreateDTO {
    private Long id;

    @NotNull(message = "Latitude cannot be empty")
    @Pattern(regexp = "^-?([0-8]?\\d(\\.\\d+)?|90(\\.0+)?)$", message = "Invalid latitude format (must be between -90 and 90)")
    private Double latitude;

    @NotNull(message = "longitude cannot be empty")
    @Pattern(regexp = "^-?((1[0-7]\\d)|(\\d{1,2}))(\\.\\d+)?|180(\\.0+)?$", message = "Invalid longitude format (must be between -180 and 180)")
    private Double longitude;

    @NotBlank(message = "Road name cannot be empty.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-/()]+$", message = "Road name can only contain letters, numbers, spaces, and basic symbols (.,' - / ())")
    @Size(min = 2, max = 100, message = "Road name must be between 2 and 100 characters.")
    private String road;

    @NotNull(message = "City cannot be empty.")
    private City city;

    @NotBlank(message = "Zip code cannot be empty.")
    @Pattern(regexp = "^\\d{4,10}$", message = "Zip code must contain only digits (4–10 characters).")
    private String zipCode;
}
