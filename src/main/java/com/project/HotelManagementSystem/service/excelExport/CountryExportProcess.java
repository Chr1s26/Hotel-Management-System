package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchQuery;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryExportProcess extends CommonExportProcess<Country, CountrySearchQuery>{
    @Autowired
    private CountryService countryService;

    @Override
    public String getSheetName() {
        return "Countries";
    }

    @Override
    public List<Country> fetchData(CountrySearchQuery query) {
        return countryService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Country>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()), null),
                new ColumnSpec<>("Name", Country::getName, null)
        );
    }
}
