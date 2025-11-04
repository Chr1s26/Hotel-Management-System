package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.region.RegionSearchQuery;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.RegionService;
import com.project.HotelManagementSystem.service.search.RegionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionExportProceess extends CommonExportProcess<Region, RegionSearchQuery> {

    @Autowired
    private RegionSearchService regionSearchService;
    @Autowired
    private RegionService regionService;

    public RegionExportProceess(ExportListingService exportListingService, FileService fileService) {
        super(exportListingService, fileService);
    }


    @Override
    public String getRecordType() {
        return Region.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Region_Listing;
    }

    @Override
    public String getSheetName() {
        return "Regions";
    }

    @Override
    public List<Region> fetchData(RegionSearchQuery query) {
        return regionSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Region>> columns() {
        return List.of(
                new ColumnSpec<>("ID",reg -> String.valueOf(reg.getId()),null),
                new ColumnSpec<>("Name",Region::getName,null),
                new ColumnSpec<>("Country",reg -> reg.getCountry() != null ? reg.getCountry().getName() : null, null),
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
