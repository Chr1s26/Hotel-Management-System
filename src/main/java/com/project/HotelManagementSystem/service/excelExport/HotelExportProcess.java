package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchQuery;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.HotelSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelExportProcess extends CommonExportProcess<Hotel, HotelSearchQuery> {

    @Autowired
    private HotelSearchService hotelSearchService;

    public HotelExportProcess(ExportListingService exportListingService, FileService fileService) {
        super(exportListingService, fileService);
    }

    @Override
    public String getRecordType() {
        return Hotel.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Hotel_Listing;
    }

    @Override
    public String getSheetName() {
        return "Hotels";
    }

    @Override
    public List<Hotel> fetchData(HotelSearchQuery query) {
        return hotelSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Hotel>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()), null),
                new ColumnSpec<>("Hotel Name", Hotel::getName,null),
                new ColumnSpec<>("Phone Number", c -> String.valueOf(c.getPhoneNumber()),null),
                new ColumnSpec<>("Email", Hotel::getEmail, null),
                new ColumnSpec<>("Rating", c -> String.valueOf(c.getRating()),null),
                new ColumnSpec<>("Hotel Type", c -> c.getHotelType() != null ? c.getHotelType().name() : "",null),
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
