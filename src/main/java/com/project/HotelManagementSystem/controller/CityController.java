package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.city.*;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.service.CityService;
import com.project.HotelManagementSystem.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String getAllCities(Model model,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false,name = "regionName") String regionName,
                               @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                               @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                               @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                               @RequestParam(defaultValue = AppConstants.SORT_ORDER) String sortOrder) {

        CitySearchCriteria criteria = new CitySearchCriteria();
        criteria.setName(name);
        criteria.setRegionName(regionName);
        criteria.setPageNumber(pageNumber);
        criteria.setPageSize(pageSize);
        criteria.setSortBy(sortBy);
        criteria.setSortOrder(sortOrder);
        CityResponse response = cityService.search(criteria);
        List<CityDTO> cities = response.getCities();
        model.addAttribute("cities", cities);
        model.addAttribute("response", response);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("regionName", regionName);
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

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel(Model model) throws IOException {
        ByteArrayInputStream in = cityService.exportCitiesToExcel();
        byte[] bytes = in.readAllBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cities.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }

    @PostMapping("/import/excel")
    public String importCitiesFromExcel(@RequestParam("file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {
        try {
            cityService.importCitiesFromExcel(file);
            redirectAttributes.addFlashAttribute("success", "Cities imported successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to import cities: " + e.getMessage());
        }
        return "redirect:/cities";
    }
}