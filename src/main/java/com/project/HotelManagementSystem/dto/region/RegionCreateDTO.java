package com.project.HotelManagementSystem.dto.region;

import com.project.HotelManagementSystem.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionCreateDTO {
    private Long id;
    private String name;
    private Country country;
}
