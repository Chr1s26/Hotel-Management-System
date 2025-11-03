package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.address.AddressSearchQuery;
import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.specification.AddressSpecification;
import com.project.HotelManagementSystem.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressSearchService {

    private final CommonSearchService commonSearchService;
    private final AddressRepository addressRepository;

    public Page<Address> searchByQuery(AddressSearchQuery query){
        return commonSearchService.searchByQuery(addressRepository, AddressSpecification::fromFilter,query);
    }

    public List<Address> searchByQueryAll(AddressSearchQuery query){
        return commonSearchService.searchByQueryAll(addressRepository,AddressSpecification::fromFilter,query);
    }
}
