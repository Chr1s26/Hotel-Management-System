package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.amenities.AmenitiesSearchQuery;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.AmenitiesSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmenitiesExportProcess extends CommonExportProcess<Amenities, AmenitiesSearchQuery> {

    @Autowired
    private AmenitiesSearchService amenitiesSearchService;

    public AmenitiesExportProcess(ExportListingService exportListingService, FileService fileService) {
        super(exportListingService, fileService);
    }

    @Override
    public String getRecordType() {
        return Amenities.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Amenities_Listing;
    }

    @Override
    public String getSheetName() {
        return "Amenities";
    }

    @Override
    public List<Amenities> fetchData(AmenitiesSearchQuery query) {
        return amenitiesSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Amenities>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()),null),
                new ColumnSpec<>("Amenities Name", Amenities::getName,null),
                new ColumnSpec<>("Description", Amenities::getDescription,null),
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
}
