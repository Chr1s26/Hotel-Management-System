package com.project.HotelManagementSystem.dto.propertyDescription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDescriptionResponse {
    private List<PropertyDescriptionDTO> propertyDescriptions;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalElements;
    private boolean LastPage;
}
