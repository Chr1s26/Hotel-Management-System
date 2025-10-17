package com.project.HotelManagementSystem.dto.country;

import lombok.Data;

@Data
public class CountrySearchCriteria {
    private String name;
    private Integer pageNumber = 0;
    private Integer pageSize = 3;
    private String sortBy = "id";
    private String sortOrder = "asc";
}
