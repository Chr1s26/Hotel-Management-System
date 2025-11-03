package com.project.HotelManagementSystem.dto.region;

import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.Data;

@Data
public class RegionSearchCriteria {
    private String name;
    private String countryName;
    private StatusType status;
    private Integer pageNumber = 0;
    private Integer pageSize = 3;
    private String sortBy = "id";
    private String sortOrder = "asc";
}
