package com.project.HotelManagementSystem.dto.city;

import com.project.HotelManagementSystem.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
    private Long id;
    private String name;
    private Region region;
}
