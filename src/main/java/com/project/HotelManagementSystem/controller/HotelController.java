package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public String getAllHotels(Model model) {
        model.addAttribute("hotels", this.hotelService.findAllHotels());
        return "hotels/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("hotel", new Hotel());
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
