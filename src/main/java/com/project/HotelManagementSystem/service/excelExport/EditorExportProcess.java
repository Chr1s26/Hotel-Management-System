package com.project.HotelManagementSystem.service.excelExport;

import com.project.HotelManagementSystem.dto.searchFilter.editor.EditorSearchQuery;
import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.service.ExportListingService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.search.EditorSearchService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EditorExportProcess extends CommonExportProcess<Editor, EditorSearchQuery> {
    private final EditorSearchService editorSearchService;

    public EditorExportProcess(ExportListingService exportListingService, FileService fileService, EditorSearchService editorSearchService, UserRepository userRepository) {
        super(exportListingService, fileService, userRepository);
        this.editorSearchService = editorSearchService;
    }

    @Override
    public String getRecordType() {
        return Editor.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Editor_Listing;
    }

    @Override
    public String getSheetName() {
        return "Editors";
    }

    @Override
    public List<Editor> fetchData(EditorSearchQuery query) {
        return editorSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Editor>> columns() {
        return List.of(
                new ColumnSpec<>("ID",e -> String.valueOf(e.getId()),null),
                new ColumnSpec<>("Name", Editor::getName,null),
                new ColumnSpec<>("Phone",Editor::getPhone,null),
                new ColumnSpec<>("Date of Birth", e -> String.valueOf(e.getDateOfBirth()),null),
                new ColumnSpec<>("Nationality",Editor::getNationality,null),
                new ColumnSpec<>("Passport Number",e-> e.getPassportNumber() != null ? e.getPassportNumber() : "-",null),
                new ColumnSpec<>("National Id Number",e-> e.getNationalIdNumber() != null ? e.getNationalIdNumber() : "-",null),
                new ColumnSpec<>("Editor Type",e->String.valueOf(e.getEditorType()),null),
                new ColumnSpec<>("Connected User account", e-> e.getUser() != null ? e.getUser().getName() : null, null),
                new ColumnSpec<>("Status", e -> e.getStatus() != null ? e.getStatus().name() : "", null),
                new ColumnSpec<>("Created at", e ->
                        e.getCreatedAt() != null ? e.getCreatedAt().toString() : "", null),
                new ColumnSpec<>("Updated at", e ->
                        e.getUpdatedAt() != null ? e.getUpdatedAt().toString() : "", null),
                new ColumnSpec<>("Created by", e ->
                        e.getCreatedBy() != null ? e.getCreatedBy().getName() : "", null),
                new ColumnSpec<>("Updated by", e ->
                        e.getUpdatedBy() != null ? e.getUpdatedBy().getName() : "", null)
        );
    }

    @Override
    public String getListingRoute() {
        return "/editors";
    }
}
