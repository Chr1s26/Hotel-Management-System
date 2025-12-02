package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.user.UserSearchQuery;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserExportProcess extends CommonExportProcess<User, UserSearchQuery> {

    @Autowired
    private UserSearchService userSearchService;

    public UserExportProcess(ExportListingService exportListingService, FileService fileService, UserRepository userRepository) {
        super(exportListingService, fileService, userRepository);
    }

    @Override
    public String getRecordType() {
        return User.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.User_Listing;
    }

    @Override
    public String getSheetName() {
        return "Users";
    }

    @Override
    public List<User> fetchData(UserSearchQuery query) {
        return userSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<User>> columns() {
        return List.of(
                new ColumnSpec<>("ID",u -> String.valueOf(u.getId()),null),
                new ColumnSpec<>("Name",User::getName,null),
                new ColumnSpec<>("Email",User::getEmail,null),
                new ColumnSpec<>("Roles",u -> u.getRoles().stream().map(Role::getRoleName).collect(Collectors.joining(", ")),null),
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
        return "/users";
    }
}
