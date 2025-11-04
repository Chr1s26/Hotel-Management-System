package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesCreateDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesResponse;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesUpdateDTO;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.amenities.AmenitiesSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.amenities.AmenitiesSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.amenities.AmenitiesSearchQuery;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.service.AmenitiesService;
import com.project.HotelManagementSystem.service.excelExport.AmenitiesExportProcess;
import com.project.HotelManagementSystem.service.search.AmenitiesSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/amenities")
@RequiredArgsConstructor
public class AmenitiesController {

    private final AmenitiesService amenitiesService;
    private final AmenitiesSearchService amenitiesSearchService;
    private final AmenitiesExportProcess amenitiesExportProcess;

    @ModelAttribute("query")
    public AmenitiesSearchQuery initQuery() {
        AmenitiesSearchQuery query = new AmenitiesSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new AmenitiesSearchFilter(AmenitiesSearchField.NAME, MatchType.CONTAINS,""),
                new AmenitiesSearchFilter(AmenitiesSearchField.STATUS, MatchType.EXACT,"")
        ));
        return query;
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("amenities", new AmenitiesCreateDTO());
        return "amenities/create";
    }

    @PostMapping("/create")
    public String createAmenities(@Valid @ModelAttribute("amenities") AmenitiesCreateDTO amenitiesCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "amenities/create";
        }
        amenitiesService.createAmenities(amenitiesCreateDTO);
        return "redirect:/amenities";
    }

    @GetMapping
    public String getAllAmenities(Model model, @ModelAttribute("query")AmenitiesSearchQuery query) {
        Page<Amenities> page = this.amenitiesSearchService.searchByQuery(query);
        model.addAttribute("amenities",  page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "amenities/listing";
    }

    @PostMapping
    public String searchAmenities(Model model, @ModelAttribute("query")AmenitiesSearchQuery query) {
        Page<Amenities> page = this.amenitiesSearchService.searchByQuery(query);
        model.addAttribute("amenities",  page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "amenities/listing";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("amenities",this.amenitiesService.findAmenitiesById(id));
        return "amenities/edit";
    }

    @PostMapping("/update/{id}")
    public String updateAmenities(@PathVariable("id") Long id,@Valid @ModelAttribute("amenities") AmenitiesUpdateDTO amenitiesUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "amenities/edit";
        }
        this.amenitiesService.updateAmenities(id, amenitiesUpdateDTO);
        return "redirect:/amenities";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteAmenities(@PathVariable("id") Long id) {
        this.amenitiesService.deleteAmenities(id);
        return "redirect:/amenities";
    }

    @PostMapping("/export/excel")
    public String exportExcelToS3(Model model, @ModelAttribute("query") AmenitiesSearchQuery query) {
        amenitiesExportProcess.generateExportFile(query);
        return "redirect:/amenities";
    }

}
