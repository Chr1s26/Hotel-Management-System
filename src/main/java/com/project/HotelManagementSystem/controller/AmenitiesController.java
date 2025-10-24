package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesCreateDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesResponse;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesUpdateDTO;
import com.project.HotelManagementSystem.service.AmenitiesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public String getAllAmenities(Model model,
                                  @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                  @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                  @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                                  @RequestParam(defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder) {
        AmenitiesResponse amenitiesResponse = this.amenitiesService.findAllAmenitiesWithPagination(pageNumber, pageSize, sortBy, sortOrder);
        List<AmenitiesDTO> amenitiesDTOS = amenitiesResponse.getAmenities();
        model.addAttribute("amenities",  amenitiesDTOS);
        model.addAttribute("response", amenitiesResponse);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
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

}
