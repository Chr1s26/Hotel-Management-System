package com.project.HotelManagementSystem.dto.searchFilter.address;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressSearchFilter {
    private AddressSearchField field;
    private MatchType matchType;
    private String value;
}
