package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionCreateDTO;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionDTO;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionResponse;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionUpdateDTO;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.propertyDescription.PropertyDescriptionSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.propertyDescription.PropertyDescriptionSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.propertyDescription.PropertyDescriptionSearchQuery;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.service.PropertyDescriptionService;
import com.project.HotelManagementSystem.service.excelExport.PropertyDescriptionExportProcess;
import com.project.HotelManagementSystem.service.search.PropertyDescriptionSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/propertyDescriptions")
public class PropertyDescriptionController {

    private final PropertyDescriptionService propertyDescriptionService;
    private final PropertyDescriptionSearchService propertyDescriptionSearchService;
    private final PropertyDescriptionExportProcess propertyDescriptionExportProcess;


    @ModelAttribute("query")
    public PropertyDescriptionSearchQuery initQuery() {
        PropertyDescriptionSearchQuery query = new PropertyDescriptionSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new PropertyDescriptionSearchFilter(PropertyDescriptionSearchField.DESCRIPTION, MatchType.CONTAINS, ""),
                new PropertyDescriptionSearchFilter(PropertyDescriptionSearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String getAllPropertyDescriptions(Model model, @ModelAttribute("query")PropertyDescriptionSearchQuery query) {
        Page<PropertyDescription> page = this.propertyDescriptionSearchService.searchByQuery(query);
        model.addAttribute("propertyDescriptions", page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "propertyDescriptions/listing";
    }

    @PostMapping
    public String searchPropertyDescription(Model model, @ModelAttribute("query")PropertyDescriptionSearchQuery query) {
        Page<PropertyDescription> page = this.propertyDescriptionSearchService.searchByQuery(query);
        model.addAttribute("propertyDescriptions", page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "propertyDescriptions/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("propertyDescription", new PropertyDescriptionCreateDTO());
        return "propertyDescriptions/create";
    }

    @PostMapping("/create")
    public String createPropertyDescription(@Valid @ModelAttribute("propertyDescription") PropertyDescriptionCreateDTO propertyDescriptionCreateDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "propertyDescriptions/create";
        }
        this.propertyDescriptionService.createPropertyDescription(propertyDescriptionCreateDTO);
        return "redirect:/propertyDescriptions";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("propertyDescription", propertyDescriptionService.findPropertyDescriptionById(id));
        return "propertyDescriptions/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePropertyDescription(@PathVariable Long id,@Valid @ModelAttribute("propertyDescription") PropertyDescriptionUpdateDTO propertyDescriptionUpdateDTO,BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "propertyDescriptions/edit";
        }
        this.propertyDescriptionService.updatePropertyDescription(id, propertyDescriptionUpdateDTO);
        return "redirect:/propertyDescriptions";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deletePropertyDescription(@PathVariable Long id, @RequestParam(name = "hard", defaultValue = "false") boolean hard, RedirectAttributes redirectAttributes) {
        if (hard) {
            this.propertyDescriptionService.deletePropertyDescription(id);
            redirectAttributes.addFlashAttribute("successMessage", "Record permanently deleted.");
        } else {
            this.propertyDescriptionService.softDeletePropertyDescription(id);
            redirectAttributes.addFlashAttribute("successMessage", "Record moved to Deleted (recoverable).");
        }
        return "redirect:/propertyDescriptions";
    }

    @PostMapping("/export/excel")
    public String exportExcelToS3(Model model, PropertyDescriptionSearchQuery query) {
        propertyDescriptionExportProcess.generateExportFile(query);
        return "redirect:/propertyDescriptions";
    }

    @GetMapping("/view/{id}")
    public String viewPropertyDescription(@PathVariable Long id, Model model) {
        PropertyDescriptionDTO propertyDescriptionDTO = this.propertyDescriptionService.findPropertyDescriptionById(id);
        model.addAttribute("propertyDescription", propertyDescriptionDTO);
        return "propertyDescriptions/view";
    }
}
