package com.project.HotelManagementSystem.dto.searchFilter.country;

import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountrySearchQuery {
    private List<CountrySearchFilter> filterList;
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private SortDirection sortDirection;
}
