package com.project.HotelManagementSystem.dto.searchFilter.policy;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicySearchFilter {
    private PolicySearchField field;
    private MatchType matchType;
    private String value;
}
