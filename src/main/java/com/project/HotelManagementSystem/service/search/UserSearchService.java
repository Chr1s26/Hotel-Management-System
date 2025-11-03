package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.user.UserSearchQuery;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.specification.UserSpecification;
import com.project.HotelManagementSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserSearchService {

    private final CommonSearchService commonSearchService;
    private final UserRepository userRepository;

    public Page<User> searchByQuery(UserSearchQuery query) {
        return commonSearchService.searchByQuery(userRepository, UserSpecification::fromFilter,query);
    }

    public List<User> searchByQueryAll(UserSearchQuery query) {
        return commonSearchService.searchByQueryAll(userRepository, UserSpecification::fromFilter,query);
    }

}
