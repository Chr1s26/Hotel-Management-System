package com.project.HotelManagementSystem.dto.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryCreateDTO {
    private Long id;
    @NotBlank(message = "country name cannot be empty")
    private String name;
}
