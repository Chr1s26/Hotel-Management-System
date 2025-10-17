package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.region.*;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.service.CountryService;
import com.project.HotelManagementSystem.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;
    private final CountryService countryService;

    @GetMapping
    public String getAllRegions(Model model,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false,name = "countryName") String countryName,
                                @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                @RequestParam(defaultValue = AppConstants.SORT_BY_Id) String sortBy,
                                @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
//        RegionResponse regionResponse = regionService.findAllRegionWithPagination(pageNumber, pageSize, sortBy, sortOrder);
        RegionSearchCriteria criteria = new RegionSearchCriteria();
        criteria.setName(name);
        criteria.setCountryName(countryName);
        criteria.setPageNumber(pageNumber);
        criteria.setPageSize(pageSize);
        criteria.setSortBy(sortBy);
        criteria.setSortOrder(sortOrder);
        RegionResponse regionResponse = this.regionService.search(criteria);
        List<RegionDTO> regionDTOList = regionResponse.getRegions();
        model.addAttribute("regions", regionDTOList);
        model.addAttribute("response", regionResponse);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("countryName", countryName);
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
    public String createRegion(@Valid @ModelAttribute("region") RegionCreateDTO regionCreateDTO, BindingResult bindingResult,Model model){
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
    public String deleteRegion(@PathVariable Long id){
        regionService.deleteRegion(id);
        return "redirect:/regions";
    }

}