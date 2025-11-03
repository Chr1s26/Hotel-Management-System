package com.project.HotelManagementSystem.dto.searchFilter.propertyDescription;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDescriptionSearchFilter {
    private PropertyDescriptionSearchField field;
    private MatchType matchType;
    private String value;
}
