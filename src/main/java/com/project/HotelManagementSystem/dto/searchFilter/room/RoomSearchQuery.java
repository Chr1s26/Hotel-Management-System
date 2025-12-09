package com.project.HotelManagementSystem.dto.searchFilter.room;

import com.project.HotelManagementSystem.dto.searchFilter.SearchQuery;

public class RoomSearchQuery extends SearchQuery<RoomSearchFilter> {
    public String getFilterValue(RoomSearchField field) {
        return this.getFilterList()
                .stream()
                .filter(f -> f.getField() == field)
                .map(RoomSearchFilter::getValue)
                .findFirst()
                .orElse("");
    }
}
