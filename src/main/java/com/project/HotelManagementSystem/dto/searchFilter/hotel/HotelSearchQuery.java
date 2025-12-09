package com.project.HotelManagementSystem.dto.searchFilter.hotel;

import com.project.HotelManagementSystem.dto.searchFilter.SearchQuery;

public class HotelSearchQuery extends SearchQuery<HotelSearchFilter> {

    public String getFilterValue(HotelSearchField field) {
        return this.getFilterList()
                .stream()
                .filter(f -> f.getField() == field)
                .map(HotelSearchFilter::getValue)
                .findFirst()
                .orElse("");
    }

}
