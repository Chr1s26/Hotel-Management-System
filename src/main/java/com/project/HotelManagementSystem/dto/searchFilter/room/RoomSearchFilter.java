package com.project.HotelManagementSystem.dto.searchFilter.room;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomSearchFilter {
    private RoomSearchField field;
    private MatchType matchType;
    private String value;
}
