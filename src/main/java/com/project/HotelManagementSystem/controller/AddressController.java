package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.address.AddressCreateDTO;
import com.project.HotelManagementSystem.dto.address.AddressDTO;
import com.project.HotelManagementSystem.dto.address.AddressResponse;
import com.project.HotelManagementSystem.dto.address.AddressUpdateDTO;
import com.project.HotelManagementSystem.service.AddressService;
import com.project.HotelManagementSystem.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;
    private final CityService cityService;

    @GetMapping
    public String getAllAddresses(Model model,
                                  @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                  @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                  @RequestParam(defaultValue = AppConstants.SORT_BY_Id, required = false) String sortBy,
                                  @RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        AddressResponse addressResponse = addressService.findAllAddressWithPagination(pageNumber, pageSize, sortBy, sortOrder);
        List<AddressDTO> addressDTOList = addressResponse.getAddresses();
        model.addAttribute("addresses", addressDTOList);
        model.addAttribute("response", addressResponse);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
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
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return "redirect:/addresses";
    }
}