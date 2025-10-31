package com.project.HotelManagementSystem.dto.searchFilter.country;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountrySearchFilter {
    private CountrySearchField field;
    private MatchType matchType;
    private String value;

}
