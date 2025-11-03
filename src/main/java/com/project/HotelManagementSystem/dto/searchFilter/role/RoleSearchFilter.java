package com.project.HotelManagementSystem.dto.searchFilter.role;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleSearchFilter {
    private RoleSearchField field;
    private MatchType matchType;
    private String value;
}
