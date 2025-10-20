package com.project.HotelManagementSystem.dto.searchFilter.editor;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditorSearchQuery {
    private List<EditorSearchFilter> filterList;
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private SortDirection sortDirection;


}
