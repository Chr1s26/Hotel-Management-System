package com.project.HotelManagementSystem.dto.user;

import com.project.HotelManagementSystem.entity.constants.StatusType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class UserSearchCriteria {
    private String name;
    private String email;
    private StatusType statusType;
    private Integer pageNumber = 0;
    private Integer pageSize = 3;
    private String sortBy = "id";
    private String sortOrder = "asc";
}
