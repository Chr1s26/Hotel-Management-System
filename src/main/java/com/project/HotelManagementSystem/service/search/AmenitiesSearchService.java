package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.amenities.AmenitiesSearchQuery;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.specification.AmenitiesSpecification;
import com.project.HotelManagementSystem.repository.AmenitiesRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AmenitiesSearchService {

    private final CommonSearchService commonSearchService;
    private final AmenitiesRepository amenitiesRepository;

    public Page<Amenities> searchByQuery(AmenitiesSearchQuery query){
        return commonSearchService.searchByQuery(amenitiesRepository, AmenitiesSpecification::fromFilter,query);
    }

    public List<Amenities> searchByQueryAll(AmenitiesSearchQuery query){
        return commonSearchService.searchByQueryAll(amenitiesRepository,AmenitiesSpecification::fromFilter,query);
    }
}
