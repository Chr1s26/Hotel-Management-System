package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.service.AddressService;
import com.project.HotelManagementSystem.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;
    private final CityService cityService;

    @GetMapping
    public String getAllAddresses(Model model){
        model.addAttribute("addresses",addressService.findAllAddress());
        return "addresses/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("address",new Address());
        model.addAttribute("cities",cityService.findAllCity());
        return "addresses/create";
    }

    @PostMapping("/create")
    public String createAddress(@ModelAttribute Address address){
        addressService.createAddress(address);
        return "redirect:/addresses";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("address",addressService.findAddressById(id));
        model.addAttribute("cities",cityService.findAllCity());
        return "addresses/edit";
    }

    @PostMapping("/update/{id}")
    public String updateAddress(@PathVariable Long id, @ModelAttribute Address address){
        addressService.updateAddress(id, address);
        return "redirect:/addresses";
    }

    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
        return "redirect:/addresses";
    }
}