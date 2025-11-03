package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchQuery;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.specification.HotelSpecification;
import com.project.HotelManagementSystem.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HotelSearchService {

    private final CommonSearchService commonSearchService;
    private final HotelRepository hotelRepository;

    public Page<Hotel> searchByQuery(HotelSearchQuery query) {
        return commonSearchService.searchByQuery(hotelRepository, HotelSpecification::fromFilter, query);
    }

    public List<Hotel> searchByQueryAll(HotelSearchQuery query) {
        return commonSearchService.searchByQueryAll(hotelRepository, HotelSpecification::fromFilter,query);
    }
}
