package com.project.HotelManagementSystem.dto.searchFilter.city;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitySearchFilter {
    private CitySearchField field;
    private MatchType matchType;
    private String value;
}
