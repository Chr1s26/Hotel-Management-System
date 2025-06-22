package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelResponse;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.service.AddressService;
import com.project.HotelManagementSystem.service.HotelService;
import com.project.HotelManagementSystem.service.PropertyDescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;
    private final AddressService addressService;
    private final PropertyDescriptionService propertyDescriptionService;

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
    public String showCreateForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("addresses", this.addressService.findAllAddress());
        model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
        return "hotels/create";
    }

    @PostMapping("/create")
    public String createHotel(@ModelAttribute Hotel hotel) {
        this.hotelService.createHotel(hotel);
        return "redirect:/hotels";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,Model model) {
        model.addAttribute("hotel", this.hotelService.findHotelById(id));
        model.addAttribute("addresses", this.addressService.findAllAddress());
        model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
        return "hotels/edit";
    }

    @PostMapping("/update/{id}")
    public String updateHotel(@PathVariable Long id, @ModelAttribute Hotel hotel) {
        this.hotelService.updateHotel(id, hotel);
        return "redirect:/hotels";
    }

    @GetMapping("/delete/{id}")
    public String deleteHotel(@PathVariable Long id) {
        this.hotelService.deleteHotel(id);
        return "redirect:/hotels";
    }
}
