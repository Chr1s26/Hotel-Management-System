package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.region.RegionSearchQuery;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.entity.specification.RegionSpecification;
import com.project.HotelManagementSystem.repository.RegionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RegionSearchService extends CommonSearchService{
    
    private final CommonSearchService commonSearchService;
    private final RegionRepository regionRepository;

    public Page<Region> searchByQuery(RegionSearchQuery query) {
        return commonSearchService.searchByQuery(regionRepository, RegionSpecification::fromFilter, query);
    }

    public List<Region> searchByQueryAll(RegionSearchQuery query) {
        return commonSearchService.searchByQueryAll(regionRepository, RegionSpecification::fromFilter, query);
    }
}
