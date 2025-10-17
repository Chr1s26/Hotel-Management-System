package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.editor.*;
import com.project.HotelManagementSystem.entity.constants.EditorType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.service.EditorService;
import com.project.HotelManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/editors")
public class EditorController {
    @Autowired
    private EditorService editorService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllEditors(Model model,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) LocalDate dateOfBirth,
                                @RequestParam(required = false) String nationality,
                                @RequestParam(required = false) String passportNumber,
                                @RequestParam(required = false) String nationalIdNumber,
                                @RequestParam(required = false) EditorType editorType,
                                @RequestParam(required = false) StatusType statusType,
                                @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                                @RequestParam(defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder) {
//        EditorResponse editorResponse = editorService.findAllEditorWithPagination(pageNumber, pageSize, sortBy, sortOrder);

        EditorSeearchCriteria criteria = new EditorSeearchCriteria();
        criteria.setName(name);
        criteria.setPhone(phone);
        criteria.setDateOfBirth(dateOfBirth);
        criteria.setNationality(nationality);
        criteria.setPassportNumber(passportNumber);
        criteria.setNationalIdNumber(nationalIdNumber);
        criteria.setEditorType(editorType);
        criteria.setStatus(statusType);
        criteria.setPageNumber(pageNumber);
        criteria.setPageSize(pageSize);
        criteria.setSortBy(sortBy);
        criteria.setSortOrder(sortOrder);
        EditorResponse editorResponse = this.editorService.search(criteria);
        List<EditorDTO> editorDTOList = editorResponse.getEditors();
        model.addAttribute("editors", editorDTOList);
        model.addAttribute("response", editorResponse);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("sortBy", sortBy);
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
        editorService.updateEditor(id, editorUpdateDTO);
        return "redirect:/editors";
    }

    @GetMapping("/delete/{id}")
    public String deleteEditor(@PathVariable Long id) {
        editorService.deleteEditor(id);
        return "redirect:/editors";
    }
}
