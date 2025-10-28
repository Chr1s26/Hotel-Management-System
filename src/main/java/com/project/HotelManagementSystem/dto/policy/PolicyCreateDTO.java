package com.project.HotelManagementSystem.dto.policy;

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
public class PolicyCreateDTO {
    private Long id;
    @NotBlank(message = "Policy title cannot be empty.")
    @Size(min = 2, max = 100, message = "Policy title must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "Policy title can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String title;
    @NotBlank(message = "Policy description cannot be empty.")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "Description can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String description;
    @NotBlank(message = "Applicable to field cannot be empty.")
    @Size(min = 2, max = 100, message = "Applicable to field must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,'\\-()]+$", message = "Applicable to field can only contain letters, numbers, spaces, and symbols (.,' - ()).")
    private String applicableTo;
}
