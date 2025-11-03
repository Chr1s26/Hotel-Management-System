package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.admin.AdminSearchQuery;
import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.specification.AdminSpecification;
import com.project.HotelManagementSystem.repository.AdminRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminSearchService {

    private final CommonSearchService commonSearchService;
    private final AdminRepository adminRepository;

    public Page<Admin> searchByQuery(AdminSearchQuery query) {
        return commonSearchService.searchByQuery(adminRepository, AdminSpecification::fromFilter,query);
    }

    public List<Admin> searchByQueryAll(AdminSearchQuery query) {
        return commonSearchService.searchByQueryAll(adminRepository, AdminSpecification::fromFilter,query);
    }
}
