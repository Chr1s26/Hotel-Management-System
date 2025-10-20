package com.project.HotelManagementSystem.dto.searchFilter.admin;

import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSearchQuery {
    private List<AdminSearchFilter> filterList;
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private SortDirection sortDirection;
}
