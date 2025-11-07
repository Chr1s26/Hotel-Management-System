package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.city.*;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.city.CitySearchField;
import com.project.HotelManagementSystem.dto.searchFilter.city.CitySearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.city.CitySearchQuery;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.service.CityService;
import com.project.HotelManagementSystem.service.RegionService;
import com.project.HotelManagementSystem.service.excelExport.CityExportProcess;
import com.project.HotelManagementSystem.service.excelImport.CityImportProcess;
import com.project.HotelManagementSystem.service.search.CitySearchService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;
    private final RegionService regionService;
    private final CityExportProcess cityExportProcess;
    private final CitySearchService citySearchService;
    private final CityImportProcess cityImportProcess;

    @ModelAttribute("query")
    public CitySearchQuery initQuery() {
        CitySearchQuery query = new CitySearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new CitySearchFilter(CitySearchField.NAME, MatchType.CONTAINS, ""),
                new CitySearchFilter(CitySearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String getAllCities(Model model, @ModelAttribute("query") CitySearchQuery query) {
        Page<City> page = citySearchService.searchByQuery(query);
        model.addAttribute("cities", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "cities/listing";
    }

    @PostMapping
    public String searchCities(Model model, @ModelAttribute("query") CitySearchQuery query) {
        Page<City> cities = this.citySearchService.searchByQuery(query);
        model.addAttribute("cities", cities.getContent());
        model.addAttribute("totalPages",cities.getTotalPages());
        model.addAttribute("totalElements",cities.getTotalElements());
        return "cities/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
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
    @ActiveRole({"ADMIN", "EDITOR"})
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
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return "redirect:/cities";
    }


    @PostMapping("/export/excel")
    public String exportExcelToS3(Model model, @ModelAttribute("query") CitySearchQuery query) throws IOException {
        cityExportProcess.generateExportFile(query);
        return "redirect:/cities";
    }


    @PostMapping("/import/excel")
    public String importCitiesFromExcel(@RequestParam("file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {
        try {
            cityImportProcess.importExcel(file);
            redirectAttributes.addFlashAttribute("success", "Cities imported successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to import cities: " + e.getMessage());
        }
        return "redirect:/cities";
    }
}