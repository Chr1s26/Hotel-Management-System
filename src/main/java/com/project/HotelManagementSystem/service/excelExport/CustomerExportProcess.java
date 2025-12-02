package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.customer.CustomerSearchQuery;
import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.CustomerSearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerExportProcess extends CommonExportProcess<Customer, CustomerSearchQuery>{

    private final CustomerSearchService customerSearchService;

    public CustomerExportProcess(ExportListingService exportListingService, FileService fileService, CustomerSearchService customerSearchService, UserRepository userRepository) {
        super(exportListingService, fileService, userRepository);
        this.customerSearchService = customerSearchService;
    }

    @Override
    public String getRecordType() {
        return Customer.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Customer_Listing;
    }

    @Override
    public String getSheetName() {
        return "Customers";
    }

    @Override
    public List<Customer> fetchData(CustomerSearchQuery query) {
        return customerSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Customer>> columns() {
        return List.of(
                new ColumnSpec<>("ID",c -> String.valueOf(c.getId()),null),
                new ColumnSpec<>("Name", Customer::getName,null),
                new ColumnSpec<>("Phone",Customer::getPhone,null),
                new ColumnSpec<>("Date of Birth", c -> String.valueOf(c.getDateOfBirth()),null),
                new ColumnSpec<>("Nationality",Customer::getNationality,null),
                new ColumnSpec<>("VIP Status", c -> c.isVipStatus() ? "VIP" : "NON-VIP", null),
                new ColumnSpec<>("Connected User account", c-> c.getUser() != null ? c.getUser().getName() : null, null),
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
        return "/customers";
    }
}
