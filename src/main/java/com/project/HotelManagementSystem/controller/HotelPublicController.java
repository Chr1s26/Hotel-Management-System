package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.service.HotelService;
import com.project.HotelManagementSystem.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@RequiredArgsConstructor
@RequestMapping("/hotelDetail")
public class HotelPublicController {
    private final HotelService hotelService;
    private final RoomTypeService roomTypeService;

    @GetMapping("/{id}")
    public String viewHotel(@PathVariable Long id, @RequestParam LocalDate checkIn, @RequestParam LocalDate checkOut, @RequestParam int guests, Model model){
        model.addAttribute("hotel",hotelService.findById(id));
        model.addAttribute("roomTypes",roomTypeService.findAvailableRoomTypes(id, checkIn, checkOut, guests));
        return "search/detail";
    }
}
