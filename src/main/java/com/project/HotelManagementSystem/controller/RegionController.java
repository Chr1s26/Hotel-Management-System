package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.service.CountryService;
import com.project.HotelManagementSystem.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;
    private final CountryService countryService;

    @GetMapping
    public String getAllRegions(Model model) {
        model.addAttribute("regions",regionService.findAllRegion());
        return "regions/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("region", new Region());
        model.addAttribute("countries", countryService.findAllCountries());
        return "regions/create";
    }

    @PostMapping("/create")
    public String createRegion(@ModelAttribute Region region){
        regionService.createRegion(region);
        return "redirect:/regions";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("region",regionService.findRegionById(id));
        model.addAttribute("countries", countryService.findAllCountries());
        return "regions/edit";
    }

    @PostMapping("/update/{id}")
    public String updateRegion(@PathVariable Long id, @ModelAttribute Region region){
        regionService.updateRegion(id, region);
        return "redirect:/regions";
    }

    @GetMapping("/delete/{id}")
    public String deleteRegion(@PathVariable Long id){
        regionService.deleteRegion(id);
        return "redirect:/regions";
    }

}