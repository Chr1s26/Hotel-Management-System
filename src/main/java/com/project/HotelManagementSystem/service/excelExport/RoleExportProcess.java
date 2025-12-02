package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.role.RoleSearchQuery;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.RoleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleExportProcess extends CommonExportProcess<Role, RoleSearchQuery> {

    @Autowired
    private RoleSearchService roleSearchService;

    public RoleExportProcess(ExportListingService exportListingService, FileService fileService, UserRepository userRepository) {
        super(exportListingService, fileService, userRepository);
    }

    @Override
    public String getRecordType() {
        return Role.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Role_Listing;
    }

    @Override
    public String getSheetName() {
        return "Roles";
    }

    @Override
    public List<Role> fetchData(RoleSearchQuery query) {
        return roleSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Role>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()),null),
                new ColumnSpec<>("Role Name", Role::getRoleName,null),
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
        return "/roles";
    }
}
