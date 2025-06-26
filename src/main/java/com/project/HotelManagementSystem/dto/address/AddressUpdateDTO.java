package com.project.HotelManagementSystem.dto.address;

import com.project.HotelManagementSystem.entity.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdateDTO {
    private Long id;

    @NotNull(message = "Latitude cannot be empty")
    private Double latitude;

    @NotNull(message = "longitude cannot be empty")
    private Double longitude;

    @NotBlank(message = "road cannot be empty")
    private String road;

    @NotNull(message = "city cannot be empty")
    private City city;

    @NotBlank(message = "zip code cannot be empty")
    @Size(min = 5, message = "zip code should have at least 5 characters")
    private String zipCode;
}
