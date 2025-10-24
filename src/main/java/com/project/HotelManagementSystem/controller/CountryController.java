package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.country.*;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public String getAllCountries(Model model,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                  @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                  @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                                  @RequestParam(defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder) {

        CountrySearchCriteria criteria = new CountrySearchCriteria();
        criteria.setName(name);
        criteria.setPageNumber(pageNumber);
        criteria.setPageSize(pageSize);
        criteria.setSortBy(sortBy);
        criteria.setSortOrder(sortOrder);
        CountryResponse countryResponse = this.countryService.search(criteria);
        List<CountryDTO> countryDTOList = countryResponse.getCountries();
        model.addAttribute("countries", countryDTOList);
        model.addAttribute("response",countryResponse);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("sortOrder",sortOrder);
        return "countries/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("country", new CountryCreateDTO());
        return "countries/create";
    }

    @PostMapping("/create")
    public String createCountry(@Valid @ModelAttribute("country") CountryCreateDTO countryCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "countries/create";
        }
        this.countryService.createCountry(countryCreateDTO);
        return "redirect:/countries";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("country", countryService.findCountryById(id));
        return "countries/edit";
    }

    @PostMapping("/update/{id}")
    public String updateCountry(@PathVariable Long id,@Valid @ModelAttribute("country") CountryUpdateDTO countryUpdateDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "countries/edit";
        }
        this.countryService.updateCountry(id, countryUpdateDTO);
        return "redirect:/countries";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteCountry(@PathVariable Long id) {
        this.countryService.deleteCountry(id);
        return "redirect:/countries";
    }
}
