package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.country.*;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchField;
import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchQuery;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.service.CountryService;
import com.project.HotelManagementSystem.service.excelExport.CountryExportProcess;
import com.project.HotelManagementSystem.service.search.CountrySearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;
    private final CountryExportProcess countryExportProcess;
    private final CountrySearchService countrySearchService;

    @ModelAttribute("query")
    public CountrySearchQuery initQuery() {
        CountrySearchQuery query = new CountrySearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new CountrySearchFilter(CountrySearchField.NAME, MatchType.CONTAINS, ""),
                new CountrySearchFilter(CountrySearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String getAllCountries(Model model, @ModelAttribute("query") CountrySearchQuery query) {
        Page<Country> page = this.countrySearchService.searchByQuery(query);
        model.addAttribute("countries", page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "countries/listing";
    }

    @PostMapping
    public String searchCountries(Model model, @ModelAttribute("query") CountrySearchQuery query) {
        Page<Country> countries = this.countrySearchService.searchByQuery(query);
        model.addAttribute("countries", countries.getContent());
        model.addAttribute("totalPages",countries.getTotalPages());
        model.addAttribute("totalElements",countries.getTotalElements());
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

    @PostMapping("/export/excel")
    public String exportExcelToS3(Model model, @ModelAttribute("query") CountrySearchQuery query) throws IOException {
        countryExportProcess.generateExportFile(query);
        return "redirect:/countries";
    }
}
