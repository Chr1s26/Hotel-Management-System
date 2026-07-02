package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.address.AddressCreateDTO;
import com.project.HotelManagementSystem.dto.address.AddressDTO;
import com.project.HotelManagementSystem.dto.address.AddressUpdateDTO;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.address.AddressSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.address.AddressSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.address.AddressSearchQuery;
import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.service.AddressService;
import com.project.HotelManagementSystem.service.CityService;
import com.project.HotelManagementSystem.service.excelExport.AddressExportProcess;
import com.project.HotelManagementSystem.service.search.AddressSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;
    private final CityService cityService;
    private final AddressSearchService addressSearchService;
    private final AddressExportProcess addressExportProcess;

    @ModelAttribute("query")
    public AddressSearchQuery initQuery() {
        AddressSearchQuery query = new AddressSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new AddressSearchFilter(AddressSearchField.LATITUDE, MatchType.EXACT,""),
                new AddressSearchFilter(AddressSearchField.LONGITUDE, MatchType.EXACT,""),
                new AddressSearchFilter(AddressSearchField.ROAD, MatchType.CONTAINS,""),
                new AddressSearchFilter(AddressSearchField.STATUS, MatchType.EXACT,""),
                new AddressSearchFilter(AddressSearchField.ZIPCODE,MatchType.EXACT,"")
        ));
        return query;
    }

    @GetMapping
    public String getAllAddresses(Model model, @ModelAttribute("query")AddressSearchQuery query) {
        Page<Address> page = this.addressSearchService.searchByQuery(query);
        model.addAttribute("addresses", page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "addresses/listing";
    }

    @PostMapping
    public String searchAddress(Model model, @ModelAttribute("query")AddressSearchQuery query) {
        Page<Address> page = this.addressSearchService.searchByQuery(query);
        model.addAttribute("addresses", page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "addresses/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("address", new AddressCreateDTO());
        model.addAttribute("cities", cityService.findAllCities());
        return "addresses/create";
    }

    @PostMapping("/create")
    public String createAddress(@Valid @ModelAttribute("address") AddressCreateDTO addressCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("address", addressCreateDTO);
            model.addAttribute("cities", cityService.findAllCities());
            return "addresses/create";
        }

        addressService.createAddress(addressCreateDTO);
        return "redirect:/addresses";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("address", addressService.findAddressById(id));
        model.addAttribute("cities", cityService.findAllCities());
        return "addresses/edit";
    }

    @PostMapping("/update/{id}")
    public String updateAddress(@PathVariable Long id,@Valid @ModelAttribute("address") AddressUpdateDTO addressUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", cityService.findAllCities());
            return "addresses/edit";
        }
        addressService.updateAddress(id, addressUpdateDTO);
        return "redirect:/addresses";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteAddress(@PathVariable Long id, @RequestParam(name = "hard", defaultValue = "false") boolean hard, RedirectAttributes redirectAttributes) {
        if (hard) {
            addressService.deleteAddress(id);
            redirectAttributes.addFlashAttribute("successMessage", "Record permanently deleted.");
        } else {
            addressService.softDeleteAddress(id);
            redirectAttributes.addFlashAttribute("successMessage", "Record moved to Deleted (recoverable).");
        }
        return "redirect:/addresses";
    }

    @PostMapping("/export/excel")
    public String exportExcelToS3(Model model, @ModelAttribute("query") AddressSearchQuery query) {
        addressExportProcess.generateExportFile(query);
        return "redirect:/addresses";
    }

    @GetMapping("/view/{id}")
    public String showView(@PathVariable Long id, Model model) {
        AddressDTO addressDTO = addressService.findAddressById(id);
        model.addAttribute("address", addressDTO);
        return "addresses/view";
    }
}