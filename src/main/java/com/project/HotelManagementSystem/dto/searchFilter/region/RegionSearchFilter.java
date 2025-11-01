package com.project.HotelManagementSystem.dto.searchFilter.region;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionSearchFilter {
    private RegionSearchField field;
    private MatchType matchType;
    private String value;
}
