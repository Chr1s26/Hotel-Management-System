package com.project.HotelManagementSystem.dto.city;

import lombok.Data;

@Data
public class CitySearchCriteria {
    private String name;
    private String regionName;
    private Integer pageNumber = 0;
    private Integer pageSize = 3;
    private String sortBy = "id";
    private String sortOrder = "asc";
}
