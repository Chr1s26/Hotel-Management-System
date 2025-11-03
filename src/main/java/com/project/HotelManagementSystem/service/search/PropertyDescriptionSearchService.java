package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.propertyDescription.PropertyDescriptionSearchQuery;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.entity.specification.PropertyDescriptionSpecification;
import com.project.HotelManagementSystem.repository.PropertyDescriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PropertyDescriptionSearchService {

    private final CommonSearchService commonSearchService;
    private final PropertyDescriptionRepository propertyDescriptionRepository;

    public Page<PropertyDescription> searchByQuery(PropertyDescriptionSearchQuery query){
        return commonSearchService.searchByQuery(propertyDescriptionRepository, PropertyDescriptionSpecification::fromFilter,query);
    }

    public List<PropertyDescription> searchByQueryAll(PropertyDescriptionSearchQuery query){
        return commonSearchService.searchByQueryAll(propertyDescriptionRepository, PropertyDescriptionSpecification::fromFilter,query);
    }
}
