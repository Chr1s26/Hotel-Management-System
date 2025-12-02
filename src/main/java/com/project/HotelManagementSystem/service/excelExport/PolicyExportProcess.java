package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.policy.PolicySearchQuery;
import com.project.HotelManagementSystem.entity.ExportListing;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.PolicySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyExportProcess extends CommonExportProcess<Policy, PolicySearchQuery> {

    @Autowired
    private PolicySearchService policySearchService;

    public PolicyExportProcess(ExportListingService exportListingService, FileService fileService, UserRepository userRepository) {
        super(exportListingService, fileService, userRepository);
    }

    @Override
    public String getRecordType() {
        return Policy.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Policy_Listing;
    }

    @Override
    public String getSheetName() {
        return "Policies";
    }

    @Override
    public List<Policy> fetchData(PolicySearchQuery query) {
        return policySearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Policy>> columns() {
        return List.of(
                new ColumnSpec<>("ID", c -> String.valueOf(c.getId()),null),
                new ColumnSpec<>("Policy Title", Policy::getTitle,null),
                new ColumnSpec<>("Description", Policy::getDescription,null),
                new ColumnSpec<>("Applicable To", Policy::getApplicableTo,null),
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
        return "/policies";
    }
}
