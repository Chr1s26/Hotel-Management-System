package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.city.CitySearchQuery;
import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchQuery;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.specification.CitySpecification;
import com.project.HotelManagementSystem.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CitySearchService extends CommonSearchService{

    private final CommonSearchService commonSearchService;
    private final CityRepository cityRepository;

    public Page<City> searchByQuery(CitySearchQuery query) {
        return commonSearchService.searchByQuery(cityRepository, CitySpecification::fromFilter,query);
    }

    public List<City> searchByQueryAll(CitySearchQuery query) {
        return commonSearchService.searchByQueryAll(cityRepository, CitySpecification::fromFilter,query);
    }
}
