package com.project.HotelManagementSystem.dto.searchFilter.user;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchFilter {
    private UserSearchField field;
    private MatchType matchType;
    private String value;
}
