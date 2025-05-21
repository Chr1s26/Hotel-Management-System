package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @GetMapping()
    public String getAllCities(Model model) {
        model.addAttribute("cities", cityService.findAllCity());
        return "cities/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("city", new City());
        return "cities/create";
    }

    @PostMapping("/create")
    public String createCity(@ModelAttribute City city) {
        cityService.createCity(city);
        return "redirect:/cities";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("city",cityService.findCityById(id));
        return "regions/edit";
    }

    @PostMapping("/update/{id}")
    public String updateCity(@PathVariable Long id, @ModelAttribute City city) {
        cityService.updateCity(id, city);
        return "redirect:/cities";
    }

    @GetMapping("/delete/{id}")
    public String deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return "redirect:/cities";
    }
}