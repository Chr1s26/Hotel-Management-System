package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.service.AmenitiesService;
import com.project.HotelManagementSystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/home")
public class RoomAmenitiesController {

    private final RoomService roomService;
    private final AmenitiesService amenitiesService;

    @GetMapping("/room-amenities/new")
    public String showCreateForm(Model model) {
        model.addAttribute("rooms", roomService.findAllRooms());
        model.addAttribute("amenities", amenitiesService.findAllAmenities());
        return "roomAmenities/add";
    }
}
