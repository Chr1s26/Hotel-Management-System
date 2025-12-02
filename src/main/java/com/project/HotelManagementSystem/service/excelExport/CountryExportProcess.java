package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchQuery;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.CountrySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class CountryExportProcess extends CommonExportProcess<Country, CountrySearchQuery>{
    @Autowired
    private CountrySearchService countrySearchService;

    public CountryExportProcess(ExportListingService exportListingService, FileService fileService, UserRepository userRepository) {
        super(exportListingService, fileService, userRepository);
    }

    @Override
    public String getRecordType() {
        return Country.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Country_Listing;
    }

    @Override
    public String getSheetName() {
        return "Countries";
    }

    @Override
    public List<Country> fetchData(CountrySearchQuery query) {
        return countrySearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Country>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()), null),
                new ColumnSpec<>("Name", Country::getName, null),
                new ColumnSpec<>("Status", c -> c.getStatus() != null ? c.getStatus().name() : "", null),
                new ColumnSpec<>("Created at", c ->
                        c.getCreatedAt() != null ? c.getCreatedAt().toString() : "", null),
                new ColumnSpec<>("Updated at", c ->
                        c.getUpdatedAt() != null ? c.getUpdatedAt().toString() : "", null),
                new ColumnSpec<>("Created by", c ->
                        c.getCreatedBy() != null ? c.getCreatedBy().getName() : "", null),
                new ColumnSpec<>("Updated by", c ->
                        c.getUpdatedBy() != null ? c.getUpdatedBy().getName() : "", null)
        );
    }

    @Override
    public String getListingRoute() {
        return "/countries";
    }
}
