package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.hotel.HotelCreateDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelResponse;
import com.project.HotelManagementSystem.dto.hotel.HotelUpdateDTO;
import com.project.HotelManagementSystem.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;
    private final AddressService addressService;
    private final PropertyDescriptionService propertyDescriptionService;
    private final PromotionService promotionService;
    private final PolicyService policyService;

    @GetMapping
    public String getAllHotels(Model model,
                               @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                               @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                               @RequestParam(defaultValue = AppConstants.SORT_BY_Id,required = false) String sortBy,
                               @RequestParam(defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder) {
        HotelResponse response = this.hotelService.findAllHotelsWithPagination(pageNumber,pageSize,sortBy,sortOrder);
        List<HotelDTO> hotels = response.getHotels();
        model.addAttribute("hotels", hotels);
        model.addAttribute("response",response);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("sortOrder",sortOrder);
        return "hotels/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("hotel", new HotelCreateDTO());
        model.addAttribute("addresses", this.addressService.findAllAddress());
        model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
        model.addAttribute("promotions", this.promotionService.findAllPromotions());
        model.addAttribute("policies", this.policyService.findAllPolicies());
        return "hotels/create";
    }

    @PostMapping("/create")
    public String createHotel(@Valid @ModelAttribute("hotel") HotelCreateDTO hotelCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("addresses", this.addressService.findAllAddress());
            model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
            model.addAttribute("promotions", this.promotionService.findAllPromotions());
            model.addAttribute("policies", this.policyService.findAllPolicies());
            return "hotels/create";
        }
        this.hotelService.createHotel(hotelCreateDTO);
        return "redirect:/hotels";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable Long id,Model model) {
        model.addAttribute("hotel", this.hotelService.findHotelById(id));
        model.addAttribute("addresses", this.addressService.findAllAddress());
        model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
        model.addAttribute("promotions", this.promotionService.findAllPromotions());
        model.addAttribute("policies", this.policyService.findAllPolicies());
        return "hotels/edit";
    }

    @PostMapping("/update/{id}")
    public String updateHotel(@PathVariable Long id,@Valid @ModelAttribute("hotel") HotelUpdateDTO hotelUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("addresses", this.addressService.findAllAddress());
            model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
            model.addAttribute("promotions", this.promotionService.findAllPromotions());
            model.addAttribute("policies", this.policyService.findAllPolicies());
        }
        this.hotelService.updateHotel(id, hotelUpdateDTO);
        return "redirect:/hotels";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteHotel(@PathVariable Long id) {
        this.hotelService.deleteHotel(id);
        return "redirect:/hotels";
    }
}
