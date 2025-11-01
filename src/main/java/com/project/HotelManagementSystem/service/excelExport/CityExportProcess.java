package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.city.CitySearchQuery;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.service.CityService;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityExportProcess extends CommonExportProcess<City, CitySearchQuery> {

    private final CityService cityService;

    public CityExportProcess(ExportListingService exportListingService, FileService fileService, CityService cityService) {
        super(exportListingService, fileService);
        this.cityService = cityService;
    }

    @Override
    public List<City> fetchData(CitySearchQuery query) {
        return cityService.searchByQueryAll(query);
    }

    @Override
    public String getRecordType() {
        return City.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.City_Listing;
    }

    @Override
    public String getSheetName() {
        return "Cities";
    }

    @Override
    public List<ColumnSpec<City>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()), null),
                new ColumnSpec<>("Name", City::getName,null),
                new ColumnSpec<>("Region", c -> c.getRegion() != null ? c.getRegion().getName() : null, null)
        );
    }
}
