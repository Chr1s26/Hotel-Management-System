package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.exportListing.ExportListingSearchQuery;
import com.project.HotelManagementSystem.entity.ExportListing;
import com.project.HotelManagementSystem.entity.specification.ExportListingSpecification;
import com.project.HotelManagementSystem.repository.ExportListingRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExportListingSearchService {

    private final CommonSearchService commonSearchService;
    private final ExportListingRepository exportListingRepository;

    public Page<ExportListing> searchByQuery(ExportListingSearchQuery query) {
        return commonSearchService.searchByQuery(exportListingRepository, ExportListingSpecification::fromFilter, query);
    }

    public List<ExportListing> searchByQueryAll(ExportListingSearchQuery query) {
        return commonSearchService.searchByQueryAll(exportListingRepository, ExportListingSpecification::fromFilter,query);
    }
}
