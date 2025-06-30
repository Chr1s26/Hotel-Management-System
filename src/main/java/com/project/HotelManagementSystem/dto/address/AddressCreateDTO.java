package com.project.HotelManagementSystem.dto.address;

import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.validator.NumericString;
import com.project.HotelManagementSystem.validator.ValidCoordinates;
import com.project.HotelManagementSystem.validator.NotIntegerString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreateDTO {
    private Long id;

    @NotNull(message = "Latitude cannot be empty")
    @ValidCoordinates(isLatitude = true, message = "Latitude must be between -90 and 90")
    @NumericString(message = "longitude cannot be string")
    private Double latitude;

    @NotNull(message = "longitude cannot be empty")
    @ValidCoordinates(isLatitude = false, message = "Longitude must be between -180 and 180")
    @NumericString(message = "longitude cannot be string")
    private Double longitude;

    @NotBlank(message = "road cannot be empty")
    @NotIntegerString(message = "road cannot be integer only")
    private String road;

    @NotNull(message = "city cannot be empty")
    private City city;

    @NotBlank(message = "zip code cannot be empty")
    @Size(min = 5, message = "zip code should have at least 5 characters")
    @NumericString(message = "zip code cannot be string")
    private String zipCode;
}
