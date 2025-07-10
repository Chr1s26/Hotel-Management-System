package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.region.RegionCreateDTO;
import com.project.HotelManagementSystem.dto.region.RegionDTO;
import com.project.HotelManagementSystem.dto.region.RegionResponse;
import com.project.HotelManagementSystem.dto.region.RegionUpdateDTO;
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
                                @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                @RequestParam(defaultValue = AppConstants.SORT_BY_Id) String sortBy,
                                @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        RegionResponse regionResponse = regionService.findAllRegionWithPagination(pageNumber, pageSize, sortBy, sortOrder);
        List<RegionDTO> regionDTOList = regionResponse.getRegions();
        model.addAttribute("regions", regionDTOList);
        model.addAttribute("response", regionResponse);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        return "regions/listing";
    }

    @GetMapping("/new")
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
    public String deleteRegion(@PathVariable Long id){
        regionService.deleteRegion(id);
        return "redirect:/regions";
    }

}