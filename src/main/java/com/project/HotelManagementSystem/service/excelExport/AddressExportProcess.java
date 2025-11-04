package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.address.AddressSearchQuery;
import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.AddressSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressExportProcess extends CommonExportProcess<Address, AddressSearchQuery> {

    @Autowired
    private AddressSearchService addressSearchService;

    public AddressExportProcess(ExportListingService exportListingService, FileService fileService) {
        super(exportListingService, fileService);
    }

    @Override
    public String getRecordType() {
        return Address.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Address_Listing;
    }

    @Override
    public String getSheetName() {
        return "Addresses";
    }

    @Override
    public List<Address> fetchData(AddressSearchQuery query) {
        return addressSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Address>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()),null),
                new ColumnSpec<>("Latitude", c -> String.valueOf(c.getLatitude()),null),
                new ColumnSpec<>("Longitude", c -> String.valueOf(c.getLongitude()),null),
                new ColumnSpec<>("Road", Address::getRoad,null),
                new ColumnSpec<>("City", c -> c.getCity() != null ? c.getCity().getName() : "", null),
                new ColumnSpec<>("Zip Code", c -> String.valueOf(c.getZipCode()),null),
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
