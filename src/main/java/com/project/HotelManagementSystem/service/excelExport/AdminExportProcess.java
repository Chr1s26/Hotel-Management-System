package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.admin.AdminSearchQuery;
import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.AdminSearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminExportProcess extends CommonExportProcess<Admin, AdminSearchQuery> {

    private final AdminSearchService adminSearchService;

    public AdminExportProcess(ExportListingService exportListingService, FileService fileService, AdminSearchService adminSearchService) {
        super(exportListingService, fileService);
        this.adminSearchService = adminSearchService;
    }

    @Override
    public String getRecordType() {
        return Admin.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Admin_Listing;
    }

    @Override
    public String getSheetName() {
        return "Admins";
    }

    @Override
    public List<Admin> fetchData(AdminSearchQuery query) {
        return adminSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Admin>> columns() {
        return List.of(
                new ColumnSpec<>("ID",a -> String.valueOf(a.getId()),null),
                new ColumnSpec<>("Name",Admin::getName,null),
                new ColumnSpec<>("Phone",Admin::getPhone,null),
                new ColumnSpec<>("Date of Birth", a -> String.valueOf(a.getDateOfBirth()),null),
                new ColumnSpec<>("Nationality",Admin::getNationality,null),
                new ColumnSpec<>("Passport Number",a-> a.getPassportNumber() != null ? a.getPassportNumber() : "-",null),
                new ColumnSpec<>("National Id Number",a-> a.getNationalIdNumber() != null ? a.getNationalIdNumber() : "-",null),
                new ColumnSpec<>("Admin Type",a->String.valueOf(a.getAdminType()),null),
                new ColumnSpec<>("Connected User account", a-> a.getUser() != null ? a.getUser().getName() : null, null),
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
