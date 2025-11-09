package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.customer.CustomerSearchQuery;
import com.project.HotelManagementSystem.entity.Customer;
import com.project.HotelManagementSystem.entity.specification.CustomerSpecification;
import com.project.HotelManagementSystem.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerSearchService {

    private final CommonSearchService commonSearchService;
    private final CustomerRepository customerRepository;

    public Page<Customer> searchByQuery(CustomerSearchQuery query){
        return commonSearchService.searchByQuery(customerRepository, CustomerSpecification::fromFilter,query);
    }

    public List<Customer> searchByQueryAll(CustomerSearchQuery query){
        return commonSearchService.searchByQueryAll(customerRepository, CustomerSpecification::fromFilter,query);
    }
}
