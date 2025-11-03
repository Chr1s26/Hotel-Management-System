package com.project.HotelManagementSystem.service.search;


import com.project.HotelManagementSystem.dto.searchFilter.policy.PolicySearchQuery;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.entity.specification.PolicySpecification;
import com.project.HotelManagementSystem.repository.PolicyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PolicySearchService {

    private final CommonSearchService commonSearchService;
    private final PolicyRepository policyRepository;

    public Page<Policy> searchByQuery(PolicySearchQuery query){
        return commonSearchService.searchByQuery(policyRepository, PolicySpecification::fromFilter, query);
    }

    public List<Policy> searchByQueryAll(PolicySearchQuery query){
        return commonSearchService.searchByQueryAll(policyRepository, PolicySpecification::fromFilter, query);
    }

}
