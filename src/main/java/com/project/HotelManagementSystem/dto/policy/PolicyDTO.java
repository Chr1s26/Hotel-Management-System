package com.project.HotelManagementSystem.dto.policy;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyDTO {
    private Long id;
    private String title;
    private String description;
    private String applicableTo;
}
