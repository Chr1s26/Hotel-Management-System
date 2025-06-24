package com.project.HotelManagementSystem.dto.policy;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyUpdateDTO {
    private Long id;
    @NotBlank(message = "policy title cannot be empty")
    private String title;
    @NotBlank(message = "policy description cannot be empty")
    private String description;
    @NotBlank(message = "applicableTO cannot be empty")
    private String applicableTo;
}
