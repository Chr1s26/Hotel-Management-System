package com.project.HotelManagementSystem.dto.searchFilter.amenities;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenitiesSearchFilter {
    private AmenitiesSearchField field;
    private MatchType matchType;
    private String value;
}
