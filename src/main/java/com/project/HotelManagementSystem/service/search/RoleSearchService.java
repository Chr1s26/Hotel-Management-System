package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.role.RoleSearchQuery;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.specification.RoleSpecification;
import com.project.HotelManagementSystem.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleSearchService {

    private final CommonSearchService commonSearchService;
    private final RoleRepository roleRepository;

    public Page<Role> searchByQuery(RoleSearchQuery query){
        return commonSearchService.searchByQuery(roleRepository, RoleSpecification::fromFilter,query);
    }

    public List<Role> searchByQueryAll(RoleSearchQuery query){
        return commonSearchService.searchByQueryAll(roleRepository, RoleSpecification::fromFilter,query);
    }
}
