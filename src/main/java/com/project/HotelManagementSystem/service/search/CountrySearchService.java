package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchQuery;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.entity.specification.CountrySpecification;
import com.project.HotelManagementSystem.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountrySearchService extends CommonSearchService{

    private final CommonSearchService commonSearchService;
    private final CountryRepository countryRepository;

    public Page<Country> searchByQuery(CountrySearchQuery query) {
        return commonSearchService.searchByQuery(countryRepository, CountrySpecification::fromFilter, query);
    }
}
