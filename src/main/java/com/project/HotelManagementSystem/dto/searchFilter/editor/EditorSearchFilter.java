package com.project.HotelManagementSystem.dto.searchFilter.editor;

import com.project.HotelManagementSystem.dto.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditorSearchFilter {
    private EditorSearchField field;
    private MatchType matchType;
    private String value;
}
