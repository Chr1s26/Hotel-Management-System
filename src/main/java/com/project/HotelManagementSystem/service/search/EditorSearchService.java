package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.editor.EditorSearchQuery;
import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.entity.specification.EditorSpecification;
import com.project.HotelManagementSystem.repository.EditorRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EditorSearchService {
    private final CommonSearchService commonSearchService;
    private final EditorRepository editorRepository;

    public Page<Editor> searchByQuery(EditorSearchQuery query){
        return commonSearchService.searchByQuery(editorRepository, EditorSpecification::fromFilter,query);
    }

    public List<Editor> searchByQueryAll(EditorSearchQuery query){
        return commonSearchService.searchByQueryAll(editorRepository, EditorSpecification::fromFilter,query);
    }
}
