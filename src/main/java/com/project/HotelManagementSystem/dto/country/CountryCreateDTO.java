package com.project.HotelManagementSystem.dto.country;

import com.project.HotelManagementSystem.validator.NotIntegerString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryCreateDTO  implements Serializable {
    private static final long serialVersionUID= 1L;
    private Long id;
    @NotBlank(message = "Country name cannot be empty.")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Country Name cannot include numbers or symbols")
    private String name;
}
