package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.city.CityCreateDTO;
import com.project.HotelManagementSystem.dto.city.CityDTO;
import com.project.HotelManagementSystem.dto.city.CityResponse;
import com.project.HotelManagementSystem.dto.city.CityUpdateDTO;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.service.CityService;
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
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;
    private final RegionService regionService;

    @GetMapping
    public String getAllCities(Model model,
                               @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                               @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                               @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                               @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        CityResponse response = cityService.findAllCitiesWithPagination(pageNumber,pageSize,sortBy,sortOrder);
        List<CityDTO> cities = response.getCities();
        model.addAttribute("cities", cities);
        model.addAttribute("response", response);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("sortBy", sortBy);
        return "cities/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("city", new CityCreateDTO());
        model.addAttribute("regions", regionService.findAllRegion());
        return "cities/create";
    }

    @PostMapping("/create")
    public String createCity(@Valid @ModelAttribute("city") CityCreateDTO cityCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("regions", regionService.findAllRegion());
            return "cities/create";
        }
        cityService.createCity(cityCreateDTO);
        return "redirect:/cities";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("city",cityService.findCityById(id));
        model.addAttribute("regions", regionService.findAllRegion());
        return "cities/edit";
    }

    @PostMapping("/update/{id}")
    public String updateCity(@PathVariable Long id,@Valid @ModelAttribute("city") CityUpdateDTO cityUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("regions", regionService.findAllRegion());
            return "cities/edit";
        }
        cityService.updateCity(id, cityUpdateDTO);
        return "redirect:/cities";
    }

    @GetMapping("/delete/{id}")
    public String deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return "redirect:/cities";
    }
}