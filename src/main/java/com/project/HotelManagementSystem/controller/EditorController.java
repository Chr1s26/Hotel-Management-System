package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.editor.*;
import com.project.HotelManagementSystem.dto.searchFilter.editor.EditorSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.editor.EditorSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.editor.EditorSearchQuery;
import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.service.EditorService;
import com.project.HotelManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/editors")
public class EditorController {
    @Autowired
    private EditorService editorService;
    @Autowired
    private UserService userService;

    @ModelAttribute("query")
    public EditorSearchQuery initQuery() {
        EditorSearchQuery query = new EditorSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new EditorSearchFilter(EditorSearchField.NAME, MatchType.CONTAINS,""),
                new EditorSearchFilter(EditorSearchField.PHONE,MatchType.CONTAINS,""),
                new EditorSearchFilter(EditorSearchField.DATE_OF_BIRTH,MatchType.EXACT,""),
                new EditorSearchFilter(EditorSearchField.NATIONALITY,MatchType.CONTAINS,""),
                new EditorSearchFilter(EditorSearchField.PASSPORT_NUMBER,MatchType.CONTAINS,""),
                new EditorSearchFilter(EditorSearchField.NATIONAL_ID_NUMBER,MatchType.CONTAINS,""),
                new EditorSearchFilter(EditorSearchField.EDITOR_TYPE,MatchType.EXACT,""),
                new EditorSearchFilter(EditorSearchField.STATUS,MatchType.EXACT,"")
        ));
        return query;
    }

    @GetMapping
    public String getAllEditors(Model model,@ModelAttribute("query") EditorSearchQuery query) {
        Page<Editor> page = editorService.searchByQuery(query);
        model.addAttribute("editors", page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "editors/listing";
    }

    @PostMapping
    public String searchEditors(@ModelAttribute("query") EditorSearchQuery query,Model model){
        Page<Editor> editors = editorService.searchByQuery(query);
        model.addAttribute("editors", editors.getContent());
        model.addAttribute("totalPages",editors.getTotalPages());
        model.addAttribute("totalElements",editors.getTotalElements());
        return "editors/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("editor", new EditorCreateDTO());
        model.addAttribute("users", userService.findAllUsers());
        return "editors/create";
    }

    @PostMapping("/create")
    public String createEditor(@Valid @ModelAttribute("editor") EditorCreateDTO editorCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            return "editors/create";
        }
        editorService.createEditor(editorCreateDTO);
        return "redirect:/editors";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("editor", editorService.findEditorById(id));
        model.addAttribute("users", userService.findAllUsers());
        return "editors/edit";
    }

    @PostMapping("/update/{id}")
    public String updateEditor(@PathVariable Long id, @Valid @ModelAttribute("editor") EditorUpdateDTO editorUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editor", editorService.findEditorById(id));
            model.addAttribute("users", userService.findAllUsers());
            return "editors/edit";
        }
        Long test = editorUpdateDTO.getUser();
        System.out.println("********");
        System.out.println("test: "+test);
        editorService.updateEditor(id, editorUpdateDTO);
        return "redirect:/editors";
    }

    @GetMapping("/delete/{id}")
    public String deleteEditor(@PathVariable Long id) {
        editorService.deleteEditor(id);
        return "redirect:/editors";
    }
}
