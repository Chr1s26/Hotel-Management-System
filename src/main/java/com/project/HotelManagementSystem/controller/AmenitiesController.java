package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.service.AmenitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/amenities")
@RequiredArgsConstructor
public class AmenitiesController {

    private final AmenitiesService amenitiesService;

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("amenities", new Amenities());
        return "amenities/create";
    }

    @PostMapping("/create")
    public String createAmenities(@ModelAttribute Amenities amenities) {
        amenitiesService.createAmenities(amenities);
        return "redirect:/amenities";
    }

    @GetMapping
    public String getAllAmenities(Model model) {
        model.addAttribute("amenities",this.amenitiesService.findAllAmenities());
        return "amenities/listing";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("amenities",this.amenitiesService.findAmenitiesById(id));
        return "amenities/edit";
    }

    @PostMapping("/update/{id}")
    public String updateAmenities(@PathVariable("id") Long id, @ModelAttribute Amenities amenities) {
        this.amenitiesService.updateAmenities(id, amenities);
        return "redirect:/amenities";
    }

    @GetMapping("/delete/{id}")
    public String deleteAmenities(@PathVariable("id") Long id) {
        this.amenitiesService.deleteAmenities(id);
        return "redirect:/amenities";
    }

}
