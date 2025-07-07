package com.project.HotelManagementSystem.dto.region;

import com.project.HotelManagementSystem.entity.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionUpdateDTO {
    private Long id;

    @NotBlank(message = "Region name can't be empty.")
    private String name;

    @NotNull(message = "Country name can't be empty.")
    private Country country;
}
