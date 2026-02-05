package com.project.HotelManagementSystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyDTO {
    private String title;
    private String description;
    private String applicableTo;
}
