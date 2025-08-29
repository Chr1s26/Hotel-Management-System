package com.project.HotelManagementSystem.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateDTO {
    private Long id;
    @NotBlank(message = "role name cannot be empty")
    private String roleName;
}
