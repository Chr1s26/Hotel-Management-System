package com.project.HotelManagementSystem.dto.searchFilter.customer;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchFilter {
    private CustomerSearchField field;
    private MatchType matchType;
    private String value;
}
