package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.region.*;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.region.RegionSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.region.RegionSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.region.RegionSearchQuery;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.service.CountryService;
import com.project.HotelManagementSystem.service.RegionService;
import com.project.HotelManagementSystem.service.excelExport.RegionExportProceess;
import com.project.HotelManagementSystem.service.search.RegionSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;
    private final CountryService countryService;
    private final RegionSearchService regionSearchService;
    private final RegionExportProceess regionExportProceess;

    @ModelAttribute("query")
    public RegionSearchQuery initQuery(){
        RegionSearchQuery query = new RegionSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new RegionSearchFilter(RegionSearchField.NAME, MatchType.CONTAINS,""),
                new RegionSearchFilter(RegionSearchField.STATUS,MatchType.EXACT,"")
        ));
        return query;
    }

    @GetMapping
    public String getAllRegions(Model model,@ModelAttribute("query") RegionSearchQuery query) {
        Page<Region> page = regionSearchService.searchByQuery(query);
        model.addAttribute("regions", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
//       model.addAttribute("countryName", countryName);
        return "regions/listing";
    }

    @PostMapping
    private String searchRegions(Model model,@ModelAttribute("query") RegionSearchQuery query) {
        Page<Region> page = regionSearchService.searchByQuery(query);
        model.addAttribute("regions", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
//       model.addAttribute("countryName", countryName);
        return "regions/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model){
        model.addAttribute("region", new RegionCreateDTO());
        model.addAttribute("countries", countryService.findAllCountries());
        return "regions/create";
    }

    @PostMapping("/create")
    public String createRegion(@Valid @ModelAttribute("region") RegionCreateDTO regionCreateDTO, BindingResult bindingResult,Model model) throws Exception {
        if(bindingResult.hasErrors()){
            model.addAttribute("countries", countryService.findAllCountries());
            return "regions/create";
        }
        regionService.createRegion(regionCreateDTO);
        return "redirect:/regions";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("region",regionService.findRegionById(id));
        model.addAttribute("countries", countryService.findAllCountries());
        return "regions/edit";
    }

    @PostMapping("/update/{id}")
    public String updateRegion(@PathVariable Long id,@Valid @ModelAttribute("region") RegionUpdateDTO regionUpdateDTO,BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("countries", countryService.findAllCountries());
            return "regions/edit";
        }
        regionService.updateRegion(id, regionUpdateDTO);
        return "redirect:/regions";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteRegion(@PathVariable Long id) throws Exception {
        regionService.deleteRegion(id);
        return "redirect:/regions";
    }

    @PostMapping("/export/excel")
    public String exportExcelToS3(Model model, @ModelAttribute("query") RegionSearchQuery query) throws IOException {
        regionExportProceess.generateExportFile(query);
        return "redirect:/regions";
    }

    @GetMapping("/view/{id}")
    public String viewRegion(@PathVariable Long id, Model model){
        RegionDTO regionDTO = regionService.findRegionById(id);
        model.addAttribute("region", regionDTO);
        return "regions/view";
    }

}