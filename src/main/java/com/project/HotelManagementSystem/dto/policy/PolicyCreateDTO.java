package com.project.HotelManagementSystem.dto.policy;

import com.project.HotelManagementSystem.validator.NotIntegerString;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyCreateDTO {
    private Long id;
    @NotBlank(message = "policy title cannot be empty")
    @NotIntegerString(message = "policy title cannot be an integer")
    private String title;
    @NotBlank(message = "policy description cannot be empty")
    @NotIntegerString(message = "policy description cannot be an integer")
    private String description;
    @NotBlank(message = "applicableTO cannot be empty")
    @NotIntegerString(message = "Applicable To cannot be an integer")
    private String applicableTo;
}
