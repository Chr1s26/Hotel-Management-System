package com.project.HotelManagementSystem.dto.searchFilter.admin;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSearchFilter {
    private AdminSearchField field;
    private MatchType matchType;
    private String value;
}
