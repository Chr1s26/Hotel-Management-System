package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.propertyDescription.PropertyDescriptionSearchQuery;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.PropertyDescriptionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyDescriptionExportProcess extends CommonExportProcess<PropertyDescription, PropertyDescriptionSearchQuery> {

    @Autowired
    private PropertyDescriptionSearchService propertyDescriptionSearchService;

    public PropertyDescriptionExportProcess(ExportListingService exportListingService, FileService fileService, UserRepository userRepository) {
        super(exportListingService, fileService, userRepository);
    }

    @Override
    public String getRecordType() {
        return PropertyDescription.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.PropertyDescription_Listing;
    }

    @Override
    public String getSheetName() {
        return "Property Descriptions";
    }

    @Override
    public List<PropertyDescription> fetchData(PropertyDescriptionSearchQuery query) {
        return propertyDescriptionSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<PropertyDescription>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()),null),
                new ColumnSpec<>("Description", PropertyDescription::getDescription, null),
                new ColumnSpec<>("Opening Date", c -> c.getOpeningDate() != null ? c.getOpeningDate().toString() : "",null),
                new ColumnSpec<>("Renovation Date", c -> c.getRenovationDate() != null ? c.getRenovationDate().toString() : "",null),
                new ColumnSpec<>("Number of Rooms", c -> String.valueOf(c.getNumberOfRooms()),null),
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
        return "/propertyDescriptions";
    }
}
