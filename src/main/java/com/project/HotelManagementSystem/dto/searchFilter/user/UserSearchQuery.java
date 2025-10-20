package com.project.HotelManagementSystem.dto.searchFilter.user;

import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchQuery {
    private List<UserSearchFilter> filterList;
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private SortDirection sortDirection;
}
