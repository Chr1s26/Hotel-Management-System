package com.project.HotelManagementSystem.dto.searchFilter.hotel;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelSearchFilter {
    private HotelSearchField field;
    private MatchType matchType;
    private String value;

    public HotelSearchFilter(HotelSearchField field, String value) {
        this.field = field;
        this.value = value;
    }


}
